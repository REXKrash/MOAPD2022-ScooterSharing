package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.adapters.ScooterArrayAdapter
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.databinding.FragmentScooterListBinding
import dk.itu.moapd.scootersharing.viewmodels.ScooterListViewModel
import dk.itu.moapd.scootersharing.viewmodels.ScooterListViewModelFactory

class ScooterListFragment : Fragment() {

    private lateinit var binding: FragmentScooterListBinding
    private lateinit var viewModel: ScooterListViewModel
    private lateinit var auth: FirebaseAuth

    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScooterListBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        val scooterDao = AppDatabase.getDatabase(requireActivity().application).scooterDao()
        val viewModelFactory =
            ScooterListViewModelFactory(ScooterRepository(scooterDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScooterListViewModel::class.java)

        binding.scootersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        setupObservers()
        requestUserPermissions()

        return view
    }

    private fun setupObservers() {
        viewModel.getAll().observe(viewLifecycleOwner) { data ->
            val arrayAdapter = ScooterArrayAdapter(data.toCollection(ArrayList())) { scooter ->
                findNavController().navigate(
                    ScooterListFragmentDirections.actionScooterListFragmentToScooterDetailsFragment(
                        scooter.id
                    )
                )
            }
            binding.scootersRecyclerView.adapter = arrayAdapter
        }
    }

    private fun requestUserPermissions() {
        val permissions: java.util.ArrayList<String> = java.util.ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        val permissionsToRequest = permissionsToRequest(permissions)

        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
    }

    private fun permissionsToRequest(permissions: java.util.ArrayList<String>): java.util.ArrayList<String> {
        val result: java.util.ArrayList<String> = java.util.ArrayList()
        for (permission in permissions)
            if (PermissionChecker.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PermissionChecker.PERMISSION_GRANTED
            )
                result.add(permission)
        return result
    }
}
