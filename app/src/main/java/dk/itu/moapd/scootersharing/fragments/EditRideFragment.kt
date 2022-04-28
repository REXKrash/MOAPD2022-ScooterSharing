package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.databinding.FragmentEditRideBinding
import dk.itu.moapd.scootersharing.viewmodels.EditViewModel
import dk.itu.moapd.scootersharing.viewmodels.EditViewModelFactory

class EditRideFragment : Fragment() {

    private lateinit var binding: FragmentEditRideBinding
    private lateinit var viewModel: EditViewModel

    private val args: EditRideFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditRideBinding.inflate(inflater, container, false)
        val view = binding.root

        val scooterDao = AppDatabase.getDatabase(requireActivity().application).scooterDao()
        val viewModelFactory =
            EditViewModelFactory(args.scooterId, ScooterRepository(scooterDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditViewModel::class.java)

        viewModel.getScooter().observe(viewLifecycleOwner) { scooter ->
            scooter?.let {
                binding.nameText.setText(scooter.name)
            }
        }

        binding.updateButton.setOnClickListener {
            if (binding.nameText.text.isNotEmpty()) {
                val name = binding.nameText.text.toString().trim()

                viewModel.updateScooter(name)
                toast("Ride successfully updated!")
            } else {
                toast("Values cannot be empty!")
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        return view
    }

    private fun toast(
        text: CharSequence,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        Toast.makeText(requireContext(), text, duration).show()
    }
}
