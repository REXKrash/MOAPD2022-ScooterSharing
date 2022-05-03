package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.adapters.RideArrayAdapter
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.databinding.FragmentRideListBinding
import dk.itu.moapd.scootersharing.viewmodels.RideListViewModel
import dk.itu.moapd.scootersharing.viewmodels.RideListViewModelFactory

class RideListFragment : Fragment() {

    private lateinit var binding: FragmentRideListBinding
    private lateinit var viewModel: RideListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentRideListBinding.inflate(inflater, container, false)
        val view = binding.root

        val rideDao = AppDatabase.getDatabase(requireActivity().application).rideDao()
        val viewModelFactory =
            RideListViewModelFactory(RideRepository(rideDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RideListViewModel::class.java)

        binding.ridesRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

        setupObservers()
        setupListeners()

        return view
    }

    private fun setupObservers() {
        viewModel.getRides().observe(viewLifecycleOwner) {
            it?.let { rides ->
                val arrayAdapter = RideArrayAdapter(rides.toCollection(ArrayList()))
                binding.ridesRecyclerView.adapter = arrayAdapter
            }
        }
    }

    private fun setupListeners() {
        binding.topAppBar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}
