package dk.itu.moapd.scootersharing.utils

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dk.itu.moapd.scootersharing.R

class ScooterArrayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    val whereTextView: TextView = itemView.findViewById(R.id.where_text)
    val timeTextView: TextView = itemView.findViewById(R.id.timestamp_text)
    val rideLayout: MaterialCardView = itemView.findViewById(R.id.ride_layout)
}
