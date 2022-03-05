package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import dk.itu.moapd.scootersharing.databinding.FragmentEditRideBinding

class EditRideFragment : Fragment() {

    private lateinit var binding: FragmentEditRideBinding
    private lateinit var viewModel: EditViewModel

    private val args: EditRideFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRideBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = EditViewModelFactory(args.scooterId, RidesDB.get(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditViewModel::class.java)

        viewModel.infoTextState.observe(viewLifecycleOwner) {
            binding.infoText.setText(it)
        }
        viewModel.nameTextState.observe(viewLifecycleOwner) {
            binding.nameText.setText(it)
        }
        viewModel.whereTextState.observe(viewLifecycleOwner) {
            binding.whereText.setText(it)
        }

        binding.updateButton.setOnClickListener {
            if (binding.nameText.text.isNotEmpty() &&
                binding.whereText.text.isNotEmpty()
            ) {
                val name = binding.nameText.text.toString().trim()
                val where = binding.whereText.text.toString().trim()

                viewModel.updateScooter(name, where)

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
