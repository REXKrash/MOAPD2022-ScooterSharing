package dk.itu.moapd.scootersharing.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.databinding.FragmentRideListBinding
import dk.itu.moapd.scootersharing.viewmodels.RideListViewModel
import dk.itu.moapd.scootersharing.viewmodels.RideListViewModelFactory

class RideListFragment : Fragment() {

    private lateinit var binding: FragmentRideListBinding
    private lateinit var viewModel: RideListViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRideListBinding.inflate(inflater, container, false)
        val view = binding.root

        val viewModelFactory = RideListViewModelFactory(requireActivity().application)
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(RideListViewModel::class.java)

        return view
    }
}
