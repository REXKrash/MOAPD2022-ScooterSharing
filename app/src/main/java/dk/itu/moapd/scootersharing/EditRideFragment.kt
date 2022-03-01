package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import dk.itu.moapd.scootersharing.databinding.FragmentEditRideBinding
import dk.itu.moapd.scootersharing.model.Scooter
import dk.itu.moapd.scootersharing.model.getInfo

class EditRideFragment : Fragment() {

    private lateinit var infoText: EditText
    private lateinit var binding: FragmentEditRideBinding

    private val scooter = Scooter(0, "", "", System.currentTimeMillis())

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditRideBinding.inflate(inflater, container, false)
        val view = binding.root

        ridesDB = RidesDB.get(requireContext())

        infoText = binding.infoText

        val nameText = binding.nameText
        val whereText = binding.whereText
        val updateButton = binding.updateButton

        updateButton.setOnClickListener {
            if (nameText.text.isNotEmpty() &&
                whereText.text.isNotEmpty()
            ) {
                val name = nameText.text.toString().trim()
                val where = whereText.text.toString().trim()

                scooter.name = name
                scooter.where = where

                nameText.text.clear()
                whereText.text.clear()

                updateUI()
            }
        }

        return view
    }

    private fun updateUI() {
        infoText.setText(scooter.getInfo())
    }

    companion object {
        lateinit var ridesDB: RidesDB
    }
}
