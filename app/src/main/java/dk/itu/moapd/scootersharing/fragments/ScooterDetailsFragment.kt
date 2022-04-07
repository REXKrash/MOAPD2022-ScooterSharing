package dk.itu.moapd.scootersharing.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.databinding.FragmentScooterDetailsBinding
import dk.itu.moapd.scootersharing.viewmodels.ScooterDetailsViewModel
import dk.itu.moapd.scootersharing.viewmodels.ScooterDetailsViewModelFactory

class ScooterDetailsFragment : Fragment() {

    private lateinit var binding: FragmentScooterDetailsBinding
    private lateinit var viewModel: ScooterDetailsViewModel

    private val args: ScooterDetailsFragmentArgs by navArgs()

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScooterDetailsBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory =
            ScooterDetailsViewModelFactory(args.scooterId, requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScooterDetailsViewModel::class.java)

        viewModel.getScooter().observe(viewLifecycleOwner) { scooter ->
            scooter?.let {
                binding.scooterNameText.text =
                    resources.getString(R.string.name) + " " + it.name
                binding.scooterLocationText.text =
                    resources.getString(R.string.location) + " " + it.where
                binding.scooterActiveText.text =
                    resources.getString(R.string.active) + " " + it.active
            }
        }
        viewModel.getCurrentRide().observe(viewLifecycleOwner) { ride ->
            if (ride != null) {
                binding.toggleActiveRideButton.text = resources.getString(R.string.end_ride)
            } else {
                binding.toggleActiveRideButton.text = resources.getString(R.string.start_ride)
            }
        }

        binding.toggleActiveRideButton.setOnClickListener {
            viewModel.toggleActiveRide()
        }
        binding.editButton.setOnClickListener {
            findNavController().navigate(
                ScooterDetailsFragmentDirections.actionScooterDetailsFragmentToEditRideFragment(args.scooterId)
            )
        }
        return view
    }
}
