package dk.itu.moapd.scootersharing.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.activities.LoginActivity
import dk.itu.moapd.scootersharing.adapters.ArrayAdapter
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding
import dk.itu.moapd.scootersharing.utils.RidesDB

class ScooterSharingFragment : Fragment() {

    private lateinit var binding: FragmentScooterSharingBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScooterSharingBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.signout -> {
                    auth.signOut()
                    startLoginActivity()
                    true
                }
                else -> false
            }
        }

        val arrayAdapter = ArrayAdapter(RidesDB.get(requireContext()).getScooters()) { scooter ->
            findNavController().navigate(
                ScooterSharingFragmentDirections.actionScooterSharingFragmentToEditRideFragment(
                    scooter.id
                )
            )
        }
        binding.scootersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
        binding.scootersRecyclerView.adapter = arrayAdapter

        binding.bottomAdd.setOnClickListener {
            findNavController().navigate(
                ScooterSharingFragmentDirections.actionScooterSharingFragmentToAddRideFragment()
            )
        }
        return view
    }

    private fun startLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }
}
