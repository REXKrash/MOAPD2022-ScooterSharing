package dk.itu.moapd.scootersharing.viewmodels

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.scootersharing.database.ScooterRepository
import kotlinx.coroutines.launch

class CameraViewModel(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
) :
    ViewModel() {

    fun updateScooterImage(imageUri: Uri) {
        viewModelScope.launch {
            val scooter = scooterRepository.getById(scooterId)

            scooter?.let {
                scooter.imageUri = imageUri.toString()
                scooterRepository.update(scooter)
            }
        }
    }
}

class CameraViewModelFactory(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CameraViewModel::class.java)) {
            return CameraViewModel(scooterId, scooterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
