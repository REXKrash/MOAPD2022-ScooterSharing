package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.*
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import kotlinx.coroutines.launch

class EditViewModel(private val scooterId: Int, application: Application) :
    AndroidViewModel(application) {

    private val repository = ScooterRepository(application)
    private var scooter: LiveData<Scooter?> = repository.findById(scooterId)

    fun getScooter(): LiveData<Scooter?> = scooter

    fun deleteScooter() {
        viewModelScope.launch {
            repository.deleteById(scooterId)
        }
    }

    fun toggleActive() {
        scooter.value?.let {
            it.active = !it.active

            viewModelScope.launch {
                repository.update(it)
            }
        }
    }

    fun updateScooter(name: String) {
        scooter.value?.let {
            it.name = name

            viewModelScope.launch {
                repository.update(it)
            }
        }
    }
}

class EditViewModelFactory(private val scooterId: Int, private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditViewModel::class.java)) {
            return EditViewModel(scooterId, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
