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
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.adapters.ScooterArrayAdapter
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.databinding.FragmentScooterListBinding
import dk.itu.moapd.scootersharing.viewmodels.ScooterListViewModel
import dk.itu.moapd.scootersharing.viewmodels.ScooterListViewModelFactory

class ScooterListFragment : Fragment() {

    private lateinit var binding: FragmentScooterListBinding
    private lateinit var viewModel: ScooterListViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentScooterListBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = FirebaseAuth.getInstance()

        val viewModelFactory =
            ScooterListViewModelFactory(ScooterRepository(requireActivity().application))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(ScooterListViewModel::class.java)

        binding.scootersRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)

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
        return view
    }
}
