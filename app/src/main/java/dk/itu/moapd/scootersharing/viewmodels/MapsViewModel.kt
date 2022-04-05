package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class MapsViewModel(application: Application) : AndroidViewModel(application) {

    private val scooterRepository = ScooterRepository(application)
    private val scooters = scooterRepository.getAll()

    fun getScooters(): LiveData<List<Scooter>> = scooters
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
