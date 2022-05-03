package dk.itu.moapd.scootersharing.viewmodels

import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.RideRepository
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.*
import kotlinx.coroutines.launch

class ScooterDetailsViewModel(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
    private val rideRepository: RideRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: LiveData<User?>

    private val scooter = scooterRepository.findById(scooterId)
    private var currentRide = MutableLiveData<Ride?>(null)
    private var lastRideValues = MutableLiveData<Pair<Double, String>?>(null)

    fun getScooter(): LiveData<Scooter?> = scooter
    fun getCurrentRide(): LiveData<Ride?> = currentRide
    fun getLastRideValues(): LiveData<Pair<Double, String>?> = lastRideValues

    init {
        auth.currentUser?.uid?.let { uid ->
            user = userRepository.findByUid(uid)
        }
    }

    fun isRideActive(): Boolean {
        currentRide.value?.let {
            return true
        }
        return false
    }

    fun resetLastRideValues() {
        lastRideValues.value = null
    }

    fun toggleActiveRide() {
        currentRide.value?.let { it ->
            val then = it.rentalTime
            val rideUid = it.userUid + "_" + scooterId + "_" + then

            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.uid?.let { uid ->
                viewModelScope.launch {
                    val ride = rideRepository.getByRideUid(rideUid)

                    ride?.let { ride2 ->

                        val duration = System.currentTimeMillis() - then
                        ride2.rentalTime = duration

                        var price = ride2.rentalTime.toDouble() / 60_000.0
                        if (price <= 10.0) {
                            price = 10.0
                        }

                        lastRideValues.value = Pair(price, ride2.getRentalTime(duration))
                        userRepository.decreaseBalance(uid, price)

                        ride2.price = price
                        ride2.status = RideStatus.FINISHED

                        rideRepository.update(ride2)
                        currentRide.value = null

                        scooter.value?.let { scooter ->
                            scooter.active = false
                            scooter.locked = true
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

                    scooter.value?.let { scooter ->
                        scooter.active = true
                        scooter.locked = false
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
    private val userRepository: UserRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScooterDetailsViewModel::class.java)) {
            return ScooterDetailsViewModel(
                scooterId,
                scooterRepository,
                rideRepository,
                userRepository
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
