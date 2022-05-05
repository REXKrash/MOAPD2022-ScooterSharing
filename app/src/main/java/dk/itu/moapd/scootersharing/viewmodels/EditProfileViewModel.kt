package dk.itu.moapd.scootersharing.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch

class EditProfileViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private lateinit var user: LiveData<User?>

    private val URL = "https://scootersharing-2022-default-rtdb.europe-west1.firebasedatabase.app/"
    private val database = Firebase.database(URL).reference

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

            database.child("users").child(it.uid).child("name").setValue(name)
            database.child("users").child(it.uid).child("email").setValue(email)
            database.child("users").child(it.uid).child("balance").setValue(balance)
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
