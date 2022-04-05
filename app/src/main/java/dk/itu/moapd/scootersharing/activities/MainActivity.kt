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
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.ActivityMainBinding
import dk.itu.moapd.scootersharing.models.Scooter
import dk.itu.moapd.scootersharing.models.User
import kotlinx.coroutines.launch
import java.util.*

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
        seedDefaultBikes()
    }

    private fun seedDefaultBikes() {
        val scooterRepository = ScooterRepository(application)
        val scooters = scooterRepository.getAll()

        scooters.observe(this) {
            if (it.isEmpty()) {
                lifecycleScope.launch {

                    scooterRepository.insert(
                        Scooter(
                            1,
                            "Scooter 1",
                            "ITU",
                            randomDate(),
                            false,
                            55.6576,
                            12.5900
                        )
                    )
                    scooterRepository.insert(
                        Scooter(
                            2,
                            "Super Scooter 2",
                            "Fields",
                            randomDate(),
                            false,
                            55.6596,
                            12.5930
                        )
                    )
                    scooterRepository.insert(
                        Scooter(
                            3,
                            "Rambo 3",
                            "Kobenhavns Lufthavn",
                            randomDate(),
                            false,
                            55.6586,
                            12.5920
                        )
                    )
                }
            }
        }
    }

    private fun randomDate(): Long {
        val random = Random()
        val now = System.currentTimeMillis()
        val year = random.nextDouble() * 1000 * 60 * 60 * 24 * 365
        return (now - year).toLong()
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
