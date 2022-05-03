package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: LiveData<User?>

    init {
        auth.currentUser?.uid?.let { uid ->
            user = userRepository.findByUid(uid)
        }
    }

    fun getUser(): LiveData<User?> = user

    fun updateUser(name: String, email: String, balance: Double) {
        user.value?.let {
            it.name = name
            it.email = email
            it.balance = balance

            viewModelScope.launch {
                userRepository.update(it)
            }
        }
    }
}

class EditProfileViewModelFactory(private val userRepository: UserRepository) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(EditProfileViewModel::class.java)) {
            return EditProfileViewModel(userRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
