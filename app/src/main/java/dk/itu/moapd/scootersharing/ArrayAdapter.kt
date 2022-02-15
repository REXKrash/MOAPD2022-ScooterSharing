package dk.itu.moapd.scootersharing

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import dk.itu.moapd.scootersharing.model.Scooter
import dk.itu.moapd.scootersharing.model.getTimestamp

class ArrayAdapter(var scooters: List<Scooter>) : RecyclerView.Adapter<ArrayViewHolder>() {

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
        }
    }
}
