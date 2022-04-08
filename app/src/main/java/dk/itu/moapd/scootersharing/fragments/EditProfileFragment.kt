package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.databinding.FragmentEditProfileBinding
import dk.itu.moapd.scootersharing.viewmodels.EditProfileViewModel
import dk.itu.moapd.scootersharing.viewmodels.EditProfileViewModelFactory

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = EditProfileViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditProfileViewModel::class.java)

        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.nameText.setText(user.name)
                binding.emailText.setText(user.email)
            }
        }

        binding.saveButton.setOnClickListener {

            val name = binding.nameText.text.toString()
            val email = binding.emailText.text.toString()

            if (name.isNotEmpty()) {
                if (email.isNotEmpty()) {
                    viewModel.updateUser(name, email)
                    toast("User profile successfully saved!")
                } else {
                    toast("Email cannot be empty!")
                }
            } else {
                toast("Name cannot be empty!")
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
