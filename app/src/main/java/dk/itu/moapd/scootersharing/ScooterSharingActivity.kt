package dk.itu.moapd.scootersharing

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.EditText
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding
import dk.itu.moapd.scootersharing.model.Scooter
import dk.itu.moapd.scootersharing.model.getInfo

class ScooterSharingActivity : AppCompatActivity() {

    private lateinit var lastAddedText: EditText
    private lateinit var binding: ActivityScooterSharingBinding

    private val scooter = Scooter("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        lastAddedText = binding.lastAddedText

        val nameText = binding.nameText
        val whereText = binding.whereText
        val addButton = binding.addButton

        addButton.setOnClickListener {
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
        lastAddedText.setText(scooter.getInfo())
    }
}
