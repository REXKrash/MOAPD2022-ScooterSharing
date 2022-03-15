package dk.itu.moapd.scootersharing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.utils.ArrayViewHolder
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.models.getTimestamp

class ArrayAdapter(var scooters: List<Scooter>, private val onClick: (Scooter) -> Unit) : RecyclerView.Adapter<ArrayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArrayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rides, parent, false)
        return ArrayViewHolder(view)
    }

    override fun getItemCount() = scooters.size

    override fun onBindViewHolder(holder: ArrayViewHolder, position: Int) {
        val scooter = scooters[position]
        holder.apply {
            nameTextView.text = scooter.name
            whereTextView.text = scooter.where
            timeTextView.text = scooter.getTimestamp()
            rideLayout.setOnClickListener { onClick(scooter) }
        }
    }
}
