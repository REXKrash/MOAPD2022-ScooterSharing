package dk.itu.moapd.scootersharing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        setContentView(R.layout.activity_main)
    }

    override fun onStart() {
        super.onStart()

        if (auth.currentUser == null) {
            startLoginActivity()
        } else {
            auth.currentUser?.uid?.let { uid ->

                val userRepository = UserRepository(application)
                val user = userRepository.findByUid(uid)

                user.observe(this) {
                    if (it == null) {
                        lifecycleScope.launch {
                            userRepository.insert(
                                User(
                                    0,
                                    uid,
                                    auth.currentUser?.displayName ?: "",
                                    auth.currentUser?.email ?: ""
                                )
                            )
                            Log.e("Debug", "Saved user with uid: $uid")
                        }
                    }
                }
            }
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}
