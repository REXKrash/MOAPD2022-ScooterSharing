package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.activities.LoginActivity
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.FragmentMenuBinding
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModel
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModelFactory
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var viewModel: MenuViewModel

    /**
     * The primary instance for receiving location updates.
     */
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    /**
     * This callback is called when `FusedLocationProviderClient` has a new `Location`.
     */
    private lateinit var locationCallback: LocationCallback

    /**
     * A set of static attributes used in this activity class.
     */
    companion object {
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        val view = binding.root

        val userDao = AppDatabase.getDatabase(requireActivity().application).userDao()
        val viewModelFactory = MenuViewModelFactory(UserRepository(userDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MenuViewModel::class.java)

        setupObservers()
        setupListeners()
        startLocationAware()

        return view
    }

    private fun setupObservers() {
        viewModel.getUser().observe(viewLifecycleOwner) {
            it?.let { user ->
                if (user.name.isNotEmpty()) {
                    binding.usernameText.text = user.name
                }
                if (user.email.isNotEmpty()) {
                    binding.emailText.text = user.email
                }
                binding.balanceText.text =
                    String.format(getString(R.string.balanceWithCurrency), user.balance.toInt())
            }
        }
        viewModel.locationState.observe(viewLifecycleOwner) {
            binding.latitudeText.text = getString(R.string.latitude) + " " + it.latitude
            binding.longitudeText.text = getString(R.string.longitude) + " " + it.longitude
            binding.locationTimeText.text = getString(R.string.location_time) + " " + it.time.toDateString()
        }
    }

    private fun setupListeners() {
        binding.editProfileButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToEditProfileFragment()
            )
        }
        binding.viewRidesButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToRideListFragment()
            )
        }
        binding.openScannerButton.setOnClickListener {
            findNavController().navigate(
                MenuFragmentDirections.actionMenuFragmentToScannerFragment()
            )
        }
        binding.logoutButton.setOnClickListener {

            val builder = AlertDialog.Builder(requireContext())
            builder.setMessage(R.string.logout_dialog)
                .setPositiveButton(
                    R.string.yes
                ) { dialog, _ ->
                    dialog.dismiss()
                    val auth = FirebaseAuth.getInstance()
                    auth.signOut()
                    startLoginActivity()
                }
                .setNegativeButton(
                    R.string.cancel
                ) { dialog, _ ->
                    dialog.dismiss()
                }
            builder.create()
            builder.show()
        }
    }

    private fun startLoginActivity() {
        val intent = Intent(requireActivity(), LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        subscribeToLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        unsubscribeToLocationUpdates()
    }

    private fun startLocationAware() {

        // Show a dialog to ask the user to allow the application to access the device's location.
        requestUserPermissions()

        // Start receiving location updates.
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireActivity())

        // Initialize the `LocationCallback`.
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                viewModel.onLocationChanged(locationResult.lastLocation)
            }
        }
    }

    private fun requestUserPermissions() {
        // An array with location-aware permissions.
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        // Check which permissions is needed to ask to the user.
        val permissionsToRequest = permissionsToRequest(permissions)

        // Show the permissions dialogs to the user.
        if (permissionsToRequest.size > 0)
            requestPermissions(
                permissionsToRequest.toTypedArray(),
                ALL_PERMISSIONS_RESULT
            )
    }

    private fun permissionsToRequest(permissions: ArrayList<String>): ArrayList<String> {
        val result: ArrayList<String> = ArrayList()
        for (permission in permissions)
            if (checkSelfPermission(
                    requireContext(),
                    permission
                ) != PermissionChecker.PERMISSION_GRANTED
            )
                result.add(permission)
        return result
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
        // Check if the user allows the application to access the location-aware resources.
        if (checkPermission())
            return

        // Sets the accuracy and desired interval for active location updates.
        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Subscribe to location changes.
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun unsubscribeToLocationUpdates() {
        // Unsubscribe to location changes.
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }

    private fun Long.toDateString(): String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}
