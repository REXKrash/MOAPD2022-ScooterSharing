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
    private val currentRide =
        rideRepository.getRideByScooterIdAndStatus(scooterId, RideStatus.RUNNING.toString())
    private var lastRideValues = MutableLiveData<Pair<Double, String>?>(null)

    private var currentRideUid: String? = null

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
            if (it.status == RideStatus.RUNNING) {
                return true
            }
        }
        return false
    }

    fun resetLastRideValues() {
        lastRideValues.value = null
    }

    fun toggleActiveRide() {
        if (isRideActive()) {
            currentRide.value?.let { it ->

                val then = it.rentalTime
                val rideUid = it.userUid + "_" + scooterId + "_" + then

                val auth = FirebaseAuth.getInstance()

                auth.currentUser?.uid?.let { uid ->
                    viewModelScope.launch {
                        rideRepository.getByRideUid(rideUid)?.let { ride ->

                            val duration = System.currentTimeMillis() - then
                            ride.rentalTime = duration

                            var price = ride.rentalTime.toDouble() / 60_000.0
                            if (price <= 10.0) {
                                price = 10.0
                            }
                            val batteryUsed = ride.rentalTime.toDouble() / 60_000.0

                            lastRideValues.value = Pair(price, ride.getRentalTime(duration))
                            userRepository.decreaseBalance(uid, price)

                            ride.price = price
                            ride.status = RideStatus.FINISHED
                            ride.batteryUsed = batteryUsed

                            rideRepository.update(ride)

                            scooter.value?.let { scooter ->
                                scooter.active = false
                                scooter.locked = true
                                scooter.batteryLevel = scooter.batteryLevel - batteryUsed

                                scooterRepository.update(scooter)
                            }
                        }
                    }
                }
            }
        } else {
            val auth = FirebaseAuth.getInstance()

            auth.currentUser?.uid?.let { uid ->

                val now = System.currentTimeMillis()

                currentRideUid = uid + "_" + scooterId + "_" + now

                val newRide = Ride(
                    id = 0,
                    rideUid = currentRideUid!!,
                    scooterId = scooterId,
                    status = RideStatus.RUNNING,
                    rentalTime = now,
                    initialLocation = "initialLocation",
                    currentLocation = "currentLocation",
                    price = 0.0,
                    userUid = uid,
                    batteryUsed = 0.0
                )
                viewModelScope.launch {
                    Log.e("Debug", "Saved ride with scooterId $scooterId")
                    rideRepository.insert(newRide)

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
