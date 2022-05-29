package dk.itu.moapd.scootersharing.viewmodels

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.CameraPosition
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class MapsViewModel(scooterRepository: ScooterRepository) : ViewModel() {

    private val scooters = scooterRepository.getAll()
    private var cameraPosition: CameraPosition? = null
    private val location = MutableLiveData<Location>()

    val locationState: LiveData<Location>
        get() = location

    fun getScooters(): LiveData<List<Scooter>> = scooters

    fun setCameraPosition(position: CameraPosition) {
        cameraPosition = position
    }

    fun getCameraPosition(): CameraPosition? = cameraPosition

    fun onLocationChanged(location: Location) {
        this.location.value = location
    }
}

class MapsViewModelFactory(private val scooterRepository: ScooterRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(scooterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
