package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import kotlinx.coroutines.launch

class EditViewModel(
    scooterId: Int,
    private val scooterRepository: ScooterRepository,
) :
    ViewModel() {

    private var scooter: LiveData<Scooter?> = scooterRepository.findById(scooterId)

    fun getScooter(): LiveData<Scooter?> = scooter

    fun updateScooter(name: String) {
        scooter.value?.let {
            it.name = name

            viewModelScope.launch {
                scooterRepository.update(it)
            }
        }
    }
}

class EditViewModelFactory(
    private val scooterId: Int,
    private val scooterRepository: ScooterRepository,
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(scooterId, scooterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
