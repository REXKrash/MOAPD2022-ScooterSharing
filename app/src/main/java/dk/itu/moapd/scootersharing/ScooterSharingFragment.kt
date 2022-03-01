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

        val cont = requireContext()
        ridesDB = RidesDB.get(cont)

        val startButton = binding.startButton
        val editButton = binding.editButton
        val listButton = binding.listRidesButton
        val arrayAdapter = ArrayAdapter(ridesDB.getScooters())

        startButton.setOnClickListener {
            findNavController().navigate(R.id.action_scooterSharingFragment_to_startRideFragment)
        }
        editButton.setOnClickListener {
            findNavController().navigate(R.id.action_scooterSharingFragment_to_editRideFragment)
        }
        listButton.setOnClickListener {
            binding.scootersRecyclerView.layoutManager =
                LinearLayoutManager(cont, RecyclerView.VERTICAL, false)
            binding.scootersRecyclerView.adapter = arrayAdapter
        }

        return view
    }

    companion object {
        lateinit var ridesDB: RidesDB
    }
}
