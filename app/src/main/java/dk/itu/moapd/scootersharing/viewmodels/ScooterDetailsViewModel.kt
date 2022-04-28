package dk.itu.moapd.scootersharing.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Ride
import dk.itu.moapd.scootersharing.models.RideStatus
import dk.itu.moapd.scootersharing.models.Scooter
import kotlinx.coroutines.launch

class ScooterDetailsViewModel(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
    private val rideRepository: RideRepository,
) : ViewModel() {

    private var scooter = scooterRepository.findById(scooterId)
    private var currentRide = MutableLiveData<Ride?>(null)

    fun getScooter(): LiveData<Scooter?> = scooter

    fun getCurrentRide(): LiveData<Ride?> = currentRide

    fun isRideActive(): Boolean {
        currentRide.value?.let {
            return true
        }
        return false
    }

    fun toggleActiveRide() {
        currentRide.value?.let { it ->
            val then = it.rentalTime
            val rideUid = it.userUid + "_" + scooterId + "_" + then

            viewModelScope.launch {
                val ride = rideRepository.getByRideUid(rideUid)

                ride?.let { ride2 ->

                    ride2.rentalTime = System.currentTimeMillis() - then

                    var price = ride2.rentalTime.toDouble() / 6_000.0
                    if (price <= 10.0) {
                        price = 10.0
                    }

                    ride2.price = price
                    ride2.status = RideStatus.FINISHED

                    rideRepository.update(ride2)
                    currentRide.value = null

                    scooter.value?.let { scooter ->
                        scooter.active = false
                        scooter.locked = true
                        viewModelScope.launch {
                            scooterRepository.update(scooter)
                        }
                    }
                }
            }
        } ?: run {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.uid?.let { uid ->

                val now = System.currentTimeMillis()
                currentRide.value = Ride(
                    id = 0,
                    rideUid = uid + "_" + scooterId + "_" + now,
                    scooterId = scooterId,
                    status = RideStatus.RUNNING,
                    rentalTime = now,
                    initialLocation = "initialLocation",
                    currentLocation = "currentLocation",
                    price = 0.0,
                    userUid = uid
                )
                viewModelScope.launch {
                    rideRepository.insert(currentRide.value!!)
                }

                scooter.value?.let { scooter ->
                    scooter.active = true
                    scooter.locked = false
                    viewModelScope.launch {
                        scooterRepository.update(scooter)
                    }
                }
            } ?: run {
                Log.e("Error", "failed to get user uid")
            }
        }
    }
}

class ScooterDetailsViewModelFactory(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
    private val rideRepository: RideRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScooterDetailsViewModel::class.java)) {
            return ScooterDetailsViewModel(scooterId, scooterRepository, rideRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
