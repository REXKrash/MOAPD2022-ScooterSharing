package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.models.Ride

class RideListViewModel(rideRepository: RideRepository) : ViewModel() {

    private lateinit var rides: LiveData<List<Ride>>

    fun getRides(): LiveData<List<Ride>> = rides

    init {
        val auth = FirebaseAuth.getInstance()

        auth.currentUser?.uid?.let { uid ->
            rides = rideRepository.getAllByUserUid(uid)
        }
    }
}

class RideListViewModelFactory(private val rideRepository: RideRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RideListViewModel::class.java)) {
            return RideListViewModel(rideRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
