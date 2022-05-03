package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.models.Ride
import kotlinx.coroutines.launch

class RideListViewModel(rideRepository: RideRepository) : ViewModel() {

    private lateinit var rides: LiveData<List<Ride>>
    fun getRides(): LiveData<List<Ride>> = rides

    init {
        val auth = FirebaseAuth.getInstance()

        auth.currentUser?.uid?.let { uid ->
            viewModelScope.launch {
                rides = rideRepository.getAllByUserUid(uid)
            }
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
