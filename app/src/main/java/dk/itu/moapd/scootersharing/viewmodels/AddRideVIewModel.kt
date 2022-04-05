package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.models.Scooter
import kotlinx.coroutines.launch
import java.util.*

class AddViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = ScooterRepository(application)

    fun addScooter(name: String, where: String) {
        val scooter = Scooter(0, name, where, randomDate(), false, 0.0, 0.0)

        viewModelScope.launch {
            repository.insert(scooter)
        }
    }

    private fun randomDate(): Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
    }
}

class AddViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
