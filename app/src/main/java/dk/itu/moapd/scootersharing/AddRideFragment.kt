package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.databinding.FragmentAddRideBinding

class AddRideFragment : Fragment() {

    private lateinit var binding: FragmentAddRideBinding
    private lateinit var viewModel: AddViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddRideBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = AddViewModelFactory(RidesDB.get(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(AddViewModel::class.java)

        binding.addButton.setOnClickListener {
            if (binding.nameText.text.isNotEmpty() &&
                binding.whereText.text.isNotEmpty()
            ) {
                val name = binding.nameText.text.toString().trim()
                val where = binding.whereText.text.toString().trim()

                viewModel.addScooter(name, where)
                findNavController().popBackStack()
            } else {
                toast("Values cannot be empty!")
            }
        }

        return view
    }

    private fun toast(
        text: CharSequence,
        duration: Int = Toast.LENGTH_SHORT
    ) {
        Toast.makeText(requireContext(), text, duration).show()
    }
}
