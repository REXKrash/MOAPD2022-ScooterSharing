package dk.itu.moapd.scootersharing.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.activities.LoginActivity
import dk.itu.moapd.scootersharing.databinding.FragmentMenuBinding
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModel
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModelFactory

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var viewModel: MenuViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = MenuViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MenuViewModel::class.java)

        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToEditProfileFragment()
            )
        }
        binding.viewRidesButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToRideListFragment()
            )
        }
        binding.logoutButton.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(R.string.logout_dialog)
                .setPositiveButton(
                    R.string.yes
                ) { dialog, _ ->
                    dialog.dismiss()
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    startLoginActivity()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
            builder.show()
        }
        return view
    }

    private fun startLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }
}
