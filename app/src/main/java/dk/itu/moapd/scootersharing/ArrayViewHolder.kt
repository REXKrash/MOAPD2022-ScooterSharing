package dk.itu.moapd.scootersharing

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ArrayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    val whereTextView: TextView = itemView.findViewById(R.id.where_text)
    val timeTextView: TextView = itemView.findViewById(R.id.timestamp_text)
}
