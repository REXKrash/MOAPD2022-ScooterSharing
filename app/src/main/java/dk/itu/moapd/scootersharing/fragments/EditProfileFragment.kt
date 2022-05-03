package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.FragmentEditProfileBinding
import dk.itu.moapd.scootersharing.viewmodels.EditProfileViewModel
import dk.itu.moapd.scootersharing.viewmodels.EditProfileViewModelFactory

class EditProfileFragment : Fragment() {

    private lateinit var binding: FragmentEditProfileBinding
    private lateinit var viewModel: EditProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val view = binding.root

        val userDao = AppDatabase.getDatabase(requireActivity().application).userDao()
        val viewModelFactory =
            EditProfileViewModelFactory(UserRepository(userDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(EditProfileViewModel::class.java)

        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers() {
        viewModel.getUser().observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.nameText.setText(user.name)
                binding.emailText.setText(user.email)
                binding.balanceText.setText(user.balance.toString())
            }
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {

            val name = binding.nameText.text.toString()
            val email = binding.emailText.text.toString()
            val balance = binding.balanceText.text.toString().toDoubleOrNull()

            if (balance != null) {
                if (name.isNotEmpty() || email.isNotEmpty()) {
                    viewModel.updateUser(name, email, balance)
                    toast(getString(R.string.user_updated))
                } else {
                    toast(getString(R.string.no_empty_values))
                }
            } else {
                toast(getString(R.string.invalid_balance))
            }
        }
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun toast(
        text: CharSequence,
        duration: Int = Toast.LENGTH_SHORT,
    ) {
        Toast.makeText(requireContext(), text, duration).show()
    }
}
