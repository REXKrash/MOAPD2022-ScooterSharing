package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MenuViewModel(application: Application) : AndroidViewModel(application)

class MenuViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
