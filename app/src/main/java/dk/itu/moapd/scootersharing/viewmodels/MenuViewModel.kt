package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.User

class MenuViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: LiveData<User?>

    fun getUser(): LiveData<User?> = user

    init {
        auth.currentUser?.uid?.let { uid ->
            user = userRepository.findByUid(uid)
        }
    }
}

class MenuViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MenuViewModel::class.java)) {
            return MenuViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
