package dk.itu.moapd.scootersharing.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomAppBar.menu.add(1, R.id.scooterListFragment, 1, "List")
        binding.bottomAppBar.menu.add(1, R.id.mapsFragment, 2, "Maps")
        binding.bottomAppBar.menu.add(1, R.id.menuFragment, 3, "Menu")

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

        NavigationUI.setupWithNavController(
            binding.bottomAppBar,
            navHostFragment!!.navController
        )
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
