package dk.itu.moapd.scootersharing

import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.TextView

class ArrayViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    val nameTextView: TextView = itemView.findViewById(R.id.name_text)
    val whereTextView: TextView = itemView.findViewById(R.id.where_text)
    val timeTextView: TextView = itemView.findViewById(R.id.timestamp_text)
}
