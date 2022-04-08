package dk.itu.moapd.scootersharing.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.models.Ride
import dk.itu.moapd.scootersharing.utils.RideArrayViewHolder

class RideArrayAdapter(var rides: List<Ride>) :
    RecyclerView.Adapter<RideArrayViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RideArrayViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_rides, parent, false)
        return RideArrayViewHolder(view)
    }

    override fun getItemCount() = rides.size

    override fun onBindViewHolder(holder: RideArrayViewHolder, position: Int) {
        val ride = rides[position]
        holder.apply {
            rideTextView.text = "Scooter " + ride.scooterId
            fromTextView.text = ride.initialLocation
            toTextView.text = ride.currentLocation
            priceTextView.text = "${ride.price} DKK"
            durationTextView.text = "${ride.rentalTime} ms"
        }
    }
}
