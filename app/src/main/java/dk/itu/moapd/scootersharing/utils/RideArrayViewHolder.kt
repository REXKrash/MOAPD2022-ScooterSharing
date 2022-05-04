package dk.itu.moapd.scootersharing.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.R

class RideArrayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val rideTextView: TextView = itemView.findViewById(R.id.ride_text)
    val fromTextView: TextView = itemView.findViewById(R.id.from_text)
    val toTextView: TextView = itemView.findViewById(R.id.to_text)
    val priceTextView: TextView = itemView.findViewById(R.id.price_text)
    val durationTextView: TextView = itemView.findViewById(R.id.duration_text)
    val batteryUsedTextView: TextView = itemView.findViewById(R.id.battery_used_text)
}
