package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.model.CameraPosition
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    private val scooterRepository = ScooterRepository(application)
    private val scooters = scooterRepository.getAll()

    private var cameraPosition: CameraPosition? = null

    fun getScooters(): LiveData<List<Scooter>> = scooters

    fun setCameraPosition(position: CameraPosition) {
        cameraPosition = position
    }

    fun getCameraPosition(): CameraPosition? = cameraPosition
}

class MapsViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            return MapsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}