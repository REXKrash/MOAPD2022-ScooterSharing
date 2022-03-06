package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.databinding.FragmentScooterSharingBinding

class ScooterSharingFragment : Fragment() {

    private lateinit var binding: FragmentScooterSharingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScooterSharingBinding.inflate(inflater, container, false)
        val view = binding.root

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
}
