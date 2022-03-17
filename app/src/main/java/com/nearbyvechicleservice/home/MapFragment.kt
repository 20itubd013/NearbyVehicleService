package com.nearbyvechicleservice.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.nearbyvechicleservice.R
import java.io.IOException
import java.util.*


open class MapFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var locationManager: LocationManager? = null
    var latitude: String? = null
    var longitude:String? = null

   private var currentLocation: Location?=null
   lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        fetchLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragement_map1, container, false)
        initializeMap()
       // addClickListener()
        return root
    }

    private fun initializeMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapff) as SupportMapFragment
       mapFragment.getMapAsync(this)
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val latLng =  LatLng(currentLocation?.latitude?:0.0, currentLocation?.longitude?:0.0)
        //val latLng =  LatLng(0.0, 0.0);
        val markerOptions = MarkerOptions().position(latLng).title("I am here!").draggable(true)
        mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng))
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5F));
        mMap.addMarker(markerOptions)
       // mMap.mapType = GoogleMap.MAP_TYPE_HYBRID
    }

    private fun addClickListener(){
        mMap.setOnMarkerDragListener(object : OnMarkerDragListener {
            override fun onMarkerDragStart(marker: Marker) {}
            override fun onMarkerDrag(marker: Marker) {}
            override fun onMarkerDragEnd(marker: Marker) {
                val latLng = marker.position
                val geocoder = Geocoder(context, Locale.getDefault())
                try {
                    val address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)[0]
                    Log.e("address",address.phone)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        })

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocation()
            }
        }
    }

    private fun fetchLocation(){
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )

        }
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener(OnSuccessListener<Location> { location ->
            if (location != null) {
                currentLocation = location
                Toast.makeText(requireContext(), currentLocation?.latitude.toString() + "" + currentLocation?.longitude, Toast.LENGTH_SHORT).show()
                val mapFragment = childFragmentManager.findFragmentById(R.id.mapff) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        })
    }
}