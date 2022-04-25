package dk.itu.moapd.scootersharing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.models.getTimestamp
import dk.itu.moapd.scootersharing.utils.ScooterArrayViewHolder

class ScooterArrayAdapter(var scooters: List<Scooter>, private val onClick: (Scooter) -> Unit) :
    RecyclerView.Adapter<ScooterArrayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScooterArrayViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_scooters, parent, false)
        return ScooterArrayViewHolder(view)
    }

    override fun getItemCount() = scooters.size

    override fun onBindViewHolder(holder: ScooterArrayViewHolder, position: Int) {
        val scooter = scooters[position]
        holder.apply {
            nameTextView.text = scooter.name
            whereTextView.text = scooter.where
            timeTextView.text = scooter.getTimestamp()
            rideLayout.setOnClickListener { onClick(scooter) }
        }
    }
}
