package dk.itu.moapd.scootersharing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddViewModel(private val ridesDB: RidesDB) : ViewModel() {

    fun addScooter(name: String, where: String) {
        ridesDB.addScooter(name, where)
    }
}

class AddViewModelFactory(private val ridesDB: RidesDB) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddViewModel::class.java)) {
            return AddViewModel(ridesDB) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
