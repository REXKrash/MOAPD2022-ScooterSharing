package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.*
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.database.ScooterRepository
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModel
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModelFactory

class MapsFragment : Fragment(), OnMapsSdkInitializedCallback {

    private lateinit var viewModel: MapsViewModel

    companion object {
        private val TAG = MapsFragment::class.qualifiedName
    }

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        // Add a marker in ITU and move the camera

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
                            .title("Scooter " + scooter.id)
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

        val viewModelFactory =
            MapsViewModelFactory(ScooterRepository(requireActivity().application))
        viewModel = ViewModelProvider(this, viewModelFactory)
            .get(MapsViewModel::class.java)

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
}
