package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.PermissionChecker
import androidx.core.content.PermissionChecker.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.activities.LoginActivity
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.UserRepository
import dk.itu.moapd.scootersharing.databinding.FragmentMenuBinding
import dk.itu.moapd.scootersharing.utils.LocationService
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModel
import dk.itu.moapd.scootersharing.viewmodels.MenuViewModelFactory
import java.text.SimpleDateFormat
import java.util.*

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var viewModel: MenuViewModel

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
        requestUserPermissions()

        binding.latitudeText.text = getString(R.string.latitude) + " " + getString(R.string.loading)
        binding.longitudeText.text = getString(R.string.longitude) + " " + getString(R.string.loading)
        binding.locationTimeText.text = getString(R.string.location_time) + " " + getString(R.string.loading)

        val filter = IntentFilter("dk.itu.moapd.scootersharing.locationIntent")
        val receiver: BroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent) {
                if (isVisible) {
                    val longitude = intent.extras!!.getDouble("longitude")
                    val latitude = intent.extras!!.getDouble("latitude")
                    val time = intent.extras!!.getLong("time")
                    binding.latitudeText.text = getString(R.string.latitude) + " " + latitude
                    binding.longitudeText.text = getString(R.string.longitude) + " " + longitude
                    binding.locationTimeText.text = getString(R.string.location_time) + " " + time.toDateString()
                }
            }
        }
        requireActivity().registerReceiver(receiver, filter)

        Intent(requireContext(), LocationService::class.java).also { intent ->
            requireActivity().startService(intent)
        }
        return view
    }

    override fun onDestroy() {
        Intent(requireContext(), LocationService::class.java).also { intent ->
            requireActivity().stopService(intent)
        }
        super.onDestroy()
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

    private fun requestUserPermissions() {
        val permissions: ArrayList<String> = ArrayList()
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION)

        val permissionsToRequest = permissionsToRequest(permissions)

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

    private fun Long.toDateString(): String {
        val date = Date(this)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
        return format.format(date)
    }
}
