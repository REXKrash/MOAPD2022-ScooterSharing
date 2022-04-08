package dk.itu.moapd.scootersharing.viewmodels

import android.app.Application
import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val userRepository = UserRepository(application)
    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: LiveData<User?>

    init {
        auth.currentUser?.uid?.let { uid ->
            user = userRepository.findByUid(uid)
        }
    }

    fun getUser() = user

    fun updateUser(name: String, email: String) {
        user.value?.let {
            it.name = name
            it.email = email

            viewModelScope.launch {
                userRepository.update(it)
            }
        }
    }
}

class EditProfileViewModelFactory(private val application: Application) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
