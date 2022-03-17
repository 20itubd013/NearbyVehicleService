package com.nearbyvechicleservice

import android.Manifest
import android.content.Intent
import android.content.IntentSender

import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import kotlinx.android.synthetic.main.activity_maps.*
import java.io.IOException
import java.util.*


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    var currentMarker: Marker? = null
    private lateinit var mMap: GoogleMap
    private var currentLocation: Location? = null
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var startMarker: Marker
    private lateinit var finishMarker: Marker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()
        initializeMap()

    }

    private fun initializeMap() {
        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapLoc) as SupportMapFragment
        mapFragment.getMapAsync(this)
        btnDone.setOnClickListener {
            val i = Intent()
            i.putExtra("lat", currentLocation?.latitude.toString())
            i.putExtra("lng", currentLocation?.longitude.toString())
            setResult(RESULT_OK, i)
            finish()
        }
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

        } else {
            val task: Task<Location> = fusedLocationProviderClient.lastLocation
            task.addOnSuccessListener(OnSuccessListener<Location> { location ->
                if (location != null) {
                    currentLocation = location
                    Toast.makeText(
                        this,
                        currentLocation?.latitude.toString() + "" + currentLocation?.longitude,
                        Toast.LENGTH_SHORT
                    ).show()
                    val mapFragment =
                        supportFragmentManager.findFragmentById(R.id.mapLoc) as SupportMapFragment
                    mapFragment.getMapAsync(this)
                }
            })
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        //mMap = googleMap

        /* val latLng = LatLng(currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0)
         //val latLng =  LatLng(0.0, 0.0);
         val markerOptions = MarkerOptions().position(latLng).title("I am here!");
         mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
         mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5F));
         mMap.addMarker(markerOptions)
         mMap.addMarker(markerOptions)
         mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
         addClickListener()*/
        mMap = googleMap
        mMap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        if (checkLocationPermission()) {
            mMap.isMyLocationEnabled = true
            mMap.uiSettings.isMyLocationButtonEnabled = true
            mMap.uiSettings.isZoomControlsEnabled = true
            getCurrentLocation()

        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }
        addClickListener()
        googleMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {
                Toast.makeText(this@MapsActivity, "Changing location", Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDrag(marker: Marker) {
                Toast.makeText(this@MapsActivity, "change location", Toast.LENGTH_SHORT).show()
            }

            override fun onMarkerDragEnd(marker: Marker) {
                /*  val latLng = marker.position
                  val geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
                  try {
                      val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
                      Log.e("address", address.phone)
                  } catch (e: IOException) {
                      e.printStackTrace()
                  }*/
                fromLocationTxt.text = marker.position.latitude.toString()

                if (marker == startMarker) {
                    setStartLocation(marker.position.latitude, marker.position.longitude, "")
                }
            }
        })
        // googleMap.setOnMarkerDragListener(this)
    }

    private fun checkLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun addClickListener() {

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        when (requestCode) {
            101 -> {
                if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                }
            }
            1 -> {
                if (grantResults.size == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation()
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun getCurrentLocation() {
        val locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = (10 * 1000).toLong()
        locationRequest.fastestInterval = 2000
        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()
        val result =
            LocationServices.getSettingsClient(this).checkLocationSettings(locationSettingsRequest)
        result.addOnCompleteListener { task ->
            try {
                val response = task.getResult(ApiException::class.java)
                if (response!!.locationSettingsStates?.isLocationPresent == true) {
                    getLastLocation()
                }
            } catch (exception: ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> try {
                        val resolvable = exception as ResolvableApiException
                        resolvable.startResolutionForResult(this, 12)
                    } catch (e: IntentSender.SendIntentException) {
                    } catch (e: ClassCastException) {
                    }
                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    }
                }
            }
        }
    }

    private fun getLastLocation() {
        if (checkLocationPermission()) {
            fusedLocationProviderClient.lastLocation
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful && task.result != null) {
                        val mLastLocation = task.result
                        setStartLocation(
                            mLastLocation?.latitude ?: 0.0,
                            mLastLocation.longitude,
                            ""
                        )
                    } else {
                        Toast.makeText(this, "No current location found", Toast.LENGTH_LONG).show()
                    }
                }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                1
            )
        }

    }

    private fun setStartLocation(lat: Double, lng: Double, addr: String) {
        var address = "Current address"
        if (addr.isEmpty()) {
            val gcd = Geocoder(this, Locale.getDefault())
            val addresses: List<Address>
            try {
                addresses = gcd.getFromLocation(lat, lng, 1)
                if (addresses.isNotEmpty()) {
                    address = addresses[0].getAddressLine(0)
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            address = addr
        }
        /* val icon = BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(this.resources, R.drawable.ic_location_on)
         )*/
        startMarker = mMap.addMarker(
            MarkerOptions()
                .position(LatLng(lat, lng))
                .title("Start Location")
                .snippet("Near $address")
                // .icon(R.drawable.ic_location_on)
                .draggable(true)
        )!!

        val cameraPosition = CameraPosition.Builder()
            .target(LatLng(lat, lng))
            .zoom(17f)
            .build()
        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
        //  fromLocationTxt.text = String.format("From: Near %s", address)
    }
}