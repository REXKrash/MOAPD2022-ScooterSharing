package dk.itu.moapd.scootersharing.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.MapsInitializer.Renderer
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import dk.itu.moapd.scootersharing.R
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModel
import dk.itu.moapd.scootersharing.viewmodels.MapsViewModelFactory

class MapsFragment : Fragment(), GoogleMap.OnMarkerClickListener, OnMapsSdkInitializedCallback {

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
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(itu, 18f))
        googleMap.mapType = GoogleMap.MAP_TYPE_NORMAL

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
        googleMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(marker: Marker): Boolean {

        // Retrieve the data from the marker.
        val clickCount = marker.tag as? Int

        // Check if a click count was set, then display the click count.
        clickCount?.let {
            val newClickCount = it + 1
            marker.tag = newClickCount
            Toast.makeText(
                requireContext(),
                "${marker.title} has been clicked $newClickCount times.",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireContext(), Renderer.LATEST, this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val viewModelFactory = MapsViewModelFactory(requireActivity().application)
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
