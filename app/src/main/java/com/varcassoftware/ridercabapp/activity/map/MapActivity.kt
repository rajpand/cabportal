package com.varcassoftware.ridercabapp.activity.map

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.Circle
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.clustering.ClusterManager
import com.google.maps.android.ktx.addCircle
import com.google.maps.android.ktx.addMarker
import com.google.maps.android.ktx.awaitMap
import com.google.maps.android.ktx.awaitMapLoad
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.map.place.Place
import com.varcassoftware.ridercabapp.activity.map.place.PlaceRenderer
import com.varcassoftware.ridercabapp.activity.map.place.PlacesReader
import com.varcassoftware.ridercabapp.databinding.ActivityMapBinding
import com.varcassoftware.ridercabapp.activity.map.fragment.BottomSheetDialog
import java.util.Locale
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory
import java.io.IOException

class MapActivity : AppCompatActivity(), BottomSheetDialog.OnClickButtonListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<BottomNavigationView>

    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapViewModel : MapViewModel

    private lateinit var clusterManager: ClusterManager<Place>
    private var circle: Circle? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapViewModel = ViewModelProvider(this,ViewModelFactory(""))[MapViewModel::class.java]

        setObserver()
        setToolbar()
        setupMap()
        setClickListener()
    }
    private fun setObserver() {
        mapViewModel._initValues.observe(this){
            if(it){
                setupBottomSheet()
                mapViewModel._initValues.postValue(false)
            }
        }
    }

    private fun setClickListener() {
        getCurrentLocationAndAddress(binding.toolbar.currentLocation)
        binding.toolbar.searchIcon.setOnClickListener {
            getCurrentLocationAndAddress(binding.toolbar.currentLocation)
        }


        geocoder = Geocoder(this)/*  binding.searchDestination.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
              override fun onQueryTextSubmit(query: String?): Boolean {
                  query?.let {
                      if (it.isNotEmpty()) {
                          loadAddressFromMap(it)
                      }
                  }
                  return true
              }

              override fun onQueryTextChange(newText: String?): Boolean {

                  return true
              }
          })*/

        binding.conformLocationButton.setOnClickListener {
            setupBottomSheet()
            binding.conformLocationButton.visibility= View.GONE
        }

    }

    private fun loadAddressFromMap(query: String) {
        try {
            val addresses = geocoder.getFromLocationName(query, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = address.getAddressLine(0) // Full address
                Toast.makeText(this, "Address: $addressText", Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this, "No address found", Toast.LENGTH_LONG).show()
            }
        } catch (e: IOException) {
            Toast.makeText(this, "Geocoder service not available", Toast.LENGTH_LONG).show()
        }
    }


    private fun getCurrentLocationAndAddress(currentLocation: AppCompatTextView) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (addresses != null) {
                        if (addresses.isNotEmpty()) {
                            val address = addresses[0].getAddressLine(0)
                            currentLocation.setText(address)
                        }
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    private fun setToolbar() {
        setSupportActionBar(binding.toolbar.customToolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        drawerToggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.toolbar.customToolbar,
            R.string.drawer_open,
            R.string.drawer_close
        )
        binding.drawerLayout.addDrawerListener(drawerToggle)
        drawerToggle.syncState()
    }

    private fun setupMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        lifecycleScope.launchWhenCreated {
            val googleMap = mapFragment.awaitMap()
            addClusteredMarkers(googleMap)
            googleMap.awaitMapLoad()
            val bounds = LatLngBounds.builder()
            places.forEach { bounds.include(it.latLng) }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 20))

            googleMap.setOnCameraMoveStartedListener {
                clusterManager.markerCollection.markers.forEach { it.alpha = 0.3f }
                clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 0.3f }
            }

            googleMap.setOnCameraIdleListener {
                clusterManager.markerCollection.markers.forEach { it.alpha = 1.0f }
                clusterManager.clusterMarkerCollection.markers.forEach { it.alpha = 1.0f }
                clusterManager.onCameraIdle()
            }
        }
    }

    private fun setupBottomSheet() {
        val bottomSheet = BottomSheetDialog()
        bottomSheet.show(supportFragmentManager, "ModalBottomSheet")
    }

    private fun setupNavigationView() {
        binding.navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home -> {
                    // Handle item 1 click
                    true
                }

                R.id.about -> {
                    // Handle item 2 click
                    true
                }

                R.id.profile -> {
                    // Handle item 3 click
                    true
                }

                R.id.settings -> {
                    // Handle item 4 click
                    true
                }

                R.id.logout -> {
                    // Handle item 5 click
                    true
                }

                else -> false
            }
        }
    }

    private fun addClusteredMarkers(googleMap: GoogleMap) {
        clusterManager = ClusterManager(this, googleMap)
        clusterManager.renderer = PlaceRenderer(this, googleMap, clusterManager)

        clusterManager.markerCollection.setInfoWindowAdapter(MarkerInfoWindowAdapter(this))
        clusterManager.addItems(places)
        clusterManager.cluster()

        clusterManager.setOnClusterItemClickListener { item ->
            addCircle(googleMap, item)
            false
        }
    }

    private fun addCircle(googleMap: GoogleMap, item: Place) {
        circle?.remove()
        circle = googleMap.addCircle {
            center(item.latLng)
            radius(1000.0)
            fillColor(ContextCompat.getColor(this@MapActivity, R.color.colorPrimaryTranslucent))
            strokeColor(ContextCompat.getColor(this@MapActivity, R.color.colorPrimary))
        }
    }

    private val bicycleIcon: BitmapDescriptor by lazy {
        val color = ContextCompat.getColor(this, R.color.colorPrimary)
        BitmapHelper.vectorToBitmap(this, R.drawable.ic_directions_bike_black_24dp, color)
    }

    private fun addMarkers(googleMap: GoogleMap) {
        places.forEach { place ->
            val marker = googleMap.addMarker {
                title(place.name)
                position(place.latLng)
                icon(bicycleIcon)
            }
            marker?.tag = place
        }
    }

    companion object {
        val TAG = MapActivity::class.java.simpleName
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun openBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickButtonClickedOn(forWhat: String,massage:String) {
        when(forWhat){
            "1SD"->{
                Toast.makeText(this@MapActivity, "On Click Button ClickedOn $massage", Toast.LENGTH_SHORT).show()
            }
            "address"->{
                Toast.makeText(this@MapActivity, "Address $massage", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
