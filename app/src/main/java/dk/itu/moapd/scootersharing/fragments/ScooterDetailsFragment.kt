package dk.itu.moapd.scootersharing.fragments

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.FragmentScooterDetailsBinding
import dk.itu.moapd.scootersharing.viewmodels.ScooterDetailsViewModel
import dk.itu.moapd.scootersharing.viewmodels.ScooterDetailsViewModelFactory
import java.text.DecimalFormat

class ScooterDetailsFragment : Fragment() {

    private lateinit var binding: FragmentScooterDetailsBinding
    private lateinit var viewModel: ScooterDetailsViewModel

    private val args: ScooterDetailsFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScooterDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val database = AppDatabase.getDatabase(requireActivity().application)
        val scooterDao = database.scooterDao()
        val rideDao = database.rideDao()
        val userDao = database.userDao()

        val viewModelFactory =
            ScooterDetailsViewModelFactory(
                args.scooterId,
                ScooterRepository(scooterDao),
                RideRepository(rideDao),
                UserRepository(userDao)
            )
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScooterDetailsViewModel::class.java)

        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers() {
        viewModel.getScooter().observe(viewLifecycleOwner) { scooter ->
            scooter?.let {
                binding.scooterNameText.text =
                    getString(R.string.name) + " " + it.name
                binding.scooterLocationText.text =
                    getString(R.string.location) + " " + it.location
                binding.scooterActiveText.text =
                    getString(R.string.active) + " " + it.active
                binding.batteryText.text =
                    getString(R.string.battery) + " " + DecimalFormat("#.##").format(it.batteryLevel)

                if (it.imageUri.isNotEmpty()) {
                    val imageUri = Uri.parse(it.imageUri)
                    binding.scooterImage.setImageURI(imageUri)
                } else {
                    binding.scooterImage.setImageResource(R.drawable.ic_baseline_image_24)
                }
            }
        }
        viewModel.getCurrentRide().observe(viewLifecycleOwner) { ride ->
            if (ride != null) {
                binding.toggleActiveRideButton.text = resources.getString(R.string.end_ride)
            } else {
                binding.toggleActiveRideButton.text = resources.getString(R.string.start_ride)
            }
        }
        viewModel.getLastRideValues().observe(viewLifecycleOwner) {
            it?.let { valuePair ->

                val price = valuePair.first
                val duration = valuePair.second

                val builder = AlertDialog.Builder(requireContext())
                val msg = String.format(
                    getString(R.string.ride_ended_details),
                    price.toInt(),
                    duration
                )
                builder.setMessage(msg)
                    .setNeutralButton(
                        R.string.close
                    ) { dialog, _ ->
                        dialog.dismiss()
                    }
                builder.create()
                builder.show()
                viewModel.resetLastRideValues()
            }
        }
    }

    private fun setupListeners() {
        binding.toggleActiveRideButton.setOnClickListener {
            if (viewModel.isRideActive()) {
                dialog(R.string.end_ride_dialog, R.string.yes, R.string.cancel) {
                    viewModel.toggleActiveRide()
                }
            } else {
                dialog(R.string.start_ride_dialog, R.string.yes, R.string.cancel) {
                    viewModel.toggleActiveRide()
                }
            }
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(
                ScooterDetailsFragmentDirections.actionScooterDetailsFragmentToEditRideFragment(args.scooterId)
            )
        }
        binding.updatePictureButton.setOnClickListener {
            findNavController().navigate(
                ScooterDetailsFragmentDirections.actionScooterDetailsFragmentToCameraFragment(args.scooterId)
            )
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun dialog(
        message: Int,
        positiveButton: Int,
        negativeButton: Int,
        positiveMethod: () -> Unit,
    ) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage(message)
            .setPositiveButton(
                positiveButton
            ) { dialog, _ ->
                dialog.dismiss()
                positiveMethod()
            }
            .setNegativeButton(
                negativeButton
            ) { dialog, _ ->
                dialog.dismiss()
            }
        builder.create()
        builder.show()
    }
}
