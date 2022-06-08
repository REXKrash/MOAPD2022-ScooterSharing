package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.*
import com.google.android.gms.maps.*
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.AppDatabase
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.utils.MapsClickListener
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModel
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModelFactory
import java.util.concurrent.TimeUnit

class MapsFragment : Fragment(), OnMapsSdkInitializedCallback {

    private lateinit var viewModel: MapsViewModel

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    private var googleMap: GoogleMap? = null

    companion object {
        private val TAG = MapsFragment::class.qualifiedName
        private const val ALL_PERMISSIONS_RESULT = 1011
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        // Add a marker in ITU and move the camera
        this.googleMap = googleMap

        val itu = LatLng(55.6596, 12.5910)
        googleMap.addMarker(
            MarkerOptions()
                .position(itu)
                .title("Marker in IT University of Copenhagen")
        )
        val pos = viewModel.getCameraPosition()
        if (pos == null) {
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itu, 18f))
        } else {
            googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(pos))
        }
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

        viewModel.setCameraPosition(googleMap.cameraPosition)

        viewModel.getScooters().observe(this) {
            if (it.isNotEmpty()) {
                for (scooter in it) {
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(LatLng(scooter.latitude, scooter.longitude))
                            .title(getString(R.string.scooter) + " " + scooter.id)
                    )
                }
            }
        }

        if (!checkPermission()) {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled = true
        }
        googleMap.setOnMarkerClickListener(
            MapsClickListener { scooterId ->
                viewModel.setCameraPosition(googleMap.cameraPosition)
                findNavController().navigate(
                    MapsFragmentDirections.actionMapsFragmentToScooterDetailsFragment(scooterId)
                )
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireContext(), Renderer.LATEST, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val scooterDao = AppDatabase.getDatabase(requireActivity().application).scooterDao()
        val viewModelFactory =
            MapsViewModelFactory(ScooterRepository(scooterDao))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MapsViewModel::class.java)

        startLocationAware()

        viewModel.locationState.observe(viewLifecycleOwner) {
            if (!viewModel.hasPlacedYourLocationMarker) {
                googleMap?.let { googleMap ->
                    val pos = LatLng(it.latitude, it.longitude)
                    googleMap.addMarker(
                        MarkerOptions()
                            .position(pos)
                            .title("Your location")
                    )
                    viewModel.hasPlacedYourLocationMarker = true
                }
            }
        }

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun checkPermission() =
        ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
            requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED

    override fun onMapsSdkInitialized(renderer: Renderer) {
        when (renderer) {
            Renderer.LATEST ->
                Log.d(TAG, "The latest version of the renderer is used.")
            Renderer.LEGACY ->
                Log.d(TAG, "The legacy version of the renderer is used.")
        }
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
        requestUserPermissions()

        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(requireActivity())

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                viewModel.onLocationChanged(locationResult.lastLocation)
            }
        }
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
            if (PermissionChecker.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PermissionChecker.PERMISSION_GRANTED
            )
                result.add(permission)
        return result
    }

    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
        if (checkPermission())
            return

        val locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(5)
            fastestInterval = TimeUnit.SECONDS.toMillis(2)
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest, locationCallback, Looper.getMainLooper()
        )
    }

    private fun unsubscribeToLocationUpdates() {
        fusedLocationProviderClient
            .removeLocationUpdates(locationCallback)
    }
}
