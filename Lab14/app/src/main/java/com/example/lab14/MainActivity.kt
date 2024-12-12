package com.example.lab14

import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class MainActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (grantResults.isNotEmpty() && requestCode == 0) {
            // Check if all permissions are granted
            val allGranted = grantResults.all { it == PackageManager.PERMISSION_GRANTED }
            if (allGranted) {
                // Reload the map if permissions granted
                loadMap()
            } else {
                // Exit app if permissions denied
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Handle window insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize map
        loadMap()
    }

    override fun onMapReady(map: GoogleMap) {
        // Check for location permissions
        val hasFineLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasCoarseLocation = ActivityCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (hasFineLocation && hasCoarseLocation) {
            // Enable current location features
            map.isMyLocationEnabled = true

            // Define locations
            val taipei101 = LatLng(25.033611, 121.565000)
            val taipeiStation = LatLng(25.047924, 121.517081)
            val middlePoint = LatLng(25.032435, 121.534905)

            // Add markers
            map.addMarker(MarkerOptions().apply {
                position(taipei101)
                title("Taipei 101")
                draggable(true)
            })

            map.addMarker(MarkerOptions().apply {
                position(taipeiStation)
                title("Taipei Station")
                draggable(true)
            })

            // Draw route line
            map.addPolyline(PolylineOptions().apply {
                add(taipei101, middlePoint, taipeiStation)
                color(Color.BLUE)
                width(10f)
            })

            // Set initial camera position
            val centerPoint = LatLng(25.035, 121.54)
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(centerPoint, 13f))
        } else {
            // Request location permission if not granted
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                0
            )
        }
    }

    private fun loadMap() {
        // Get map fragment and initialize it asynchronously
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
}
