package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter

class ScooterListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScooterRepository(application)
    private val data: LiveData<List<Scooter>> = repository.getAll()

    fun getAll(): LiveData<List<Scooter>> = data
}

class ScooterListViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScooterListViewModel::class.java)) {
            return ScooterListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
