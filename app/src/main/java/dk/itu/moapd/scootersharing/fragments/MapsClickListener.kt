package dk.itu.moapd.scootersharing.fragments

import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

class MapsClickListener(private val onClick: (Int) -> Unit) : GoogleMap.OnMarkerClickListener {

    override fun onMarkerClick(marker: Marker): Boolean {
        marker.title?.let {
            if (it.contains("Scooter")) {
                val id = it.split("Scooter ")[1].toInt()
                onClick(id)
            }
        }
        return false
    }
}
