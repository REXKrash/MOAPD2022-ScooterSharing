package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterListViewModel(scooterRepository: ScooterRepository) : ViewModel() {

    private val data: LiveData<List<Scooter>> = scooterRepository.getAll()
    fun getAll(): LiveData<List<Scooter>> = data
}

class ScooterListViewModelFactory(private val scooterRepository: ScooterRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScooterListViewModel::class.java)) {
            return ScooterListViewModel(scooterRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
