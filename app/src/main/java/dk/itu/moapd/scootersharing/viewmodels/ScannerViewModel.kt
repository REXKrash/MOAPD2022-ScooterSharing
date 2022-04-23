package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class ScannerViewModel(application: Application) : AndroidViewModel(application) {

    private val scooterRepository = ScooterRepository(application)
    private lateinit var scooter: LiveData<Scooter?>

    fun getScooter() = scooter

    fun parseScooter(text: String) {
        if (text.contains("q=") && text.contains(",")) {
            val coords = text.split("q=")[1].split(",")
            val latitude = coords[0].toDouble()
            val longitude = coords[1].toDouble()

            scooter = scooterRepository.findByLongLat(longitude, latitude)
        }
    }
}

class ScannerViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScannerViewModel::class.java)) {
            return ScannerViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
