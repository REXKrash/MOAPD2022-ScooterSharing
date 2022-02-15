package dk.itu.moapd.scootersharing

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import dk.itu.moapd.scootersharing.databinding.ActivityScooterSharingBinding

class ScooterSharingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScooterSharingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScooterSharingBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ridesDB = RidesDB.get(this)

        val startButton = binding.startButton
        val editButton = binding.editButton
        val listButton = binding.listRidesButton
        val arrayAdapter = ArrayAdapter(ridesDB.getScooters())

        startButton.setOnClickListener {
            val intent = Intent(this, StartRideActivity::class.java)
            startActivity(intent)
        }
        editButton.setOnClickListener {
            val intent = Intent(this, EditRideActivity::class.java)
            startActivity(intent)
        }
        listButton.setOnClickListener {
            binding.scootersRecyclerView.layoutManager =
                LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            binding.scootersRecyclerView.adapter = arrayAdapter
        }
    }

    companion object {
        lateinit var ridesDB: RidesDB
    }
}
