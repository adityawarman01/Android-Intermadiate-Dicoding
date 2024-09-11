package com.example.submissionintermediate.view.maps

import android.content.pm.PackageManager
import android.content.res.Resources
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.submissionintermediate.R
import com.example.submissionintermediate.ViewModelFactory
import com.example.submissionintermediate.database.repository.ResultState
import com.example.submissionintermediate.database.response.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.submissionintermediate.databinding.ActivityMapsBinding
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.example.submissionintermediate.database.response.ListStoryItem as ListStoryIte

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel>{
       ViewModelFactory.getInstance(this,true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true

        // Add a marker in Sydney and move the camera
//        val dicodingSpcae = LatLng(-6.8957643, 107.6338462)
//        mMap.addMarker(MarkerOptions()
//            .position(dicodingSpcae)
//            .title("Dicoding Space")
//            .snippet("Batik Kumeli No.50"))
//        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(dicodingSpcae, 15f))

        addManyMarker()
        setMapStyle()
        getMyLocation()
    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()){
        isGranted: Boolean ->
        if (isGranted){
            getMyLocation()
        }
    }
    private fun getMyLocation(){
        if (ContextCompat.checkSelfPermission(
            this.applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )== PackageManager.PERMISSION_GRANTED
            ){
            mMap.isMyLocationEnabled = true
        }else{
            requestPermissionLauncher.launch(
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
        }

    }


    private fun addManyMarker(){
        viewModel.locationStories().observe(this) { users ->
            if (users != null) {
                when(users){
                    is ResultState.Success ->{
                    val locationUser: List<ListStoryItem> = users.data.listStory
                    val boundsBuilder = LatLngBounds.Builder()

                    locationUser.forEach {

                    val latLng = LatLng(it.lat as Double, it.lon as Double)
                   mMap.addMarker(
                    MarkerOptions()
                        .position(latLng)
                        .title(it.name)
                        .snippet(it.description))
                    boundsBuilder.include(latLng)


                }
                val bounds: LatLngBounds = boundsBuilder.build()
                mMap.animateCamera(
                    CameraUpdateFactory.newLatLngBounds(
                        bounds,
                        resources.displayMetrics.widthPixels,
                        resources.displayMetrics.heightPixels,
                        300
                    )
                )
                }
                    else -> {
                    }
                }
        }

        }
    }

    private fun setMapStyle(){
        try {
            val success= mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style))
        if (!success){
            Log.e(TAG, "Style failed")
        }
        }catch (exception: Resources.NotFoundException){
            Log.e(TAG, "Can't find style, error:", exception)
        }
    }

    companion object{
        private const val TAG = "MapsActivity"
    }
}