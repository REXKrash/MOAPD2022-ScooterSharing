package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import dk.itu.moapd.scootersharing.databinding.ActivityEditRideBinding
import dk.itu.moapd.scootersharing.model.Scooter
import dk.itu.moapd.scootersharing.model.getInfo

class EditRideActivity : AppCompatActivity() {

    private lateinit var infoText: EditText
    private lateinit var binding: ActivityEditRideBinding

    private val scooter = Scooter("", "", System.currentTimeMillis())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRideBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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
    }

    private fun updateUI() {
        infoText.setText(scooter.getInfo())
    }
}
