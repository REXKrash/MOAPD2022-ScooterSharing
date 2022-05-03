package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterListViewModel(scooterRepository: ScooterRepository) : ViewModel() {

    private val scooters = scooterRepository.getAll()
    fun getAll(): LiveData<List<Scooter>> = scooters
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
