package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CameraViewModel : ViewModel()

class CameraViewModelFactory :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
