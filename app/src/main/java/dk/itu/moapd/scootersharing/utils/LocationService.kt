package dk.itu.moapd.scootersharing.utils

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit

class LocationService : Service() {

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback

    override fun onBind(p0: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e("Debug", "Starting locationService")
        startLocationAware()
        subscribeToLocationUpdates()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        Log.e("Debug", "Stopping locationService")
        unsubscribeToLocationUpdates()
        super.onDestroy()
    }

    private fun startLocationAware() {
        fusedLocationProviderClient = LocationServices
            .getFusedLocationProviderClient(this)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                val loc = locationResult.lastLocation

                val intent = Intent("dk.itu.moapd.scootersharing.locationIntent")
                intent.putExtra("latitude", loc.latitude)
                intent.putExtra("longitude", loc.longitude)
                intent.putExtra("time", loc.time)
                sendBroadcast(intent)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun subscribeToLocationUpdates() {
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
