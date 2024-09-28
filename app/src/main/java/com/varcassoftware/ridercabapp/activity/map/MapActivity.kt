package com.varcassoftware.ridercabapp.activity.map

import android.Manifest
import android.content.pm.ActivityInfo
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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.PolylineOptions
import com.google.maps.android.PolyUtil
import com.varcassoftware.ridercabapp.activity.map.entity.DirectionsResponse
import com.varcassoftware.ridercabapp.networkResponse.RetrofitBuilder
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.utility.GlobalClasses
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class MapActivity : AppCompatActivity(), BottomSheetDialog.OnClickButtonListener {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<BottomNavigationView>

    private var _binding: ActivityMapBinding? = null
    private val binding get() = _binding!!
    private lateinit var mapViewModel: MapViewModel

    private lateinit var clusterManager: ClusterManager<Place>
    private var circle: Circle? = null
    private lateinit var drawerToggle: ActionBarDrawerToggle
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder

    private lateinit var googleMap: GoogleMap
    private lateinit var currentLocation: LatLng
    private lateinit var destinationLocation: LatLng

    private val places: List<Place> by lazy {
        PlacesReader(this).read()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mapViewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[MapViewModel::class.java]
        setObserver()
        setToolbar()
        setupMap()
        setClickListener()
    }

    private fun setObserver() {
        mapViewModel._initValues.observe(this) {
            if (it) {
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
        geocoder = Geocoder(this)
        binding.conformLocationButton.setOnClickListener {
            setupBottomSheet()
            binding.conformLocationButton.visibility = View.GONE
        }

    }


    private fun getCurrentLocationAndAddress(currentLocationForTextView: AppCompatTextView) {
        if (ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                location?.let {
                    currentLocation = LatLng(it.latitude, it.longitude)
                    val geocoder = Geocoder(this, Locale.getDefault())
                    val addresses = geocoder.getFromLocation(it.latitude, it.longitude, 1)
                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0].getAddressLine(0)
                        currentLocationForTextView.text = address
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getCurrentLocationAndAddress(binding.toolbar.currentLocation)
            }
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
            googleMap = mapFragment.awaitMap()
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
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun openBottomSheet() {
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onClickButtonClickedOn(forWhat: String, massage: String) {
        when (forWhat) {
            "1SD" -> {
                Toast.makeText(
                    this@MapActivity, "On Click Button ClickedOn $massage", Toast.LENGTH_SHORT
                ).show()
            }
            "address" -> {
                Toast.makeText(this@MapActivity, "Address $massage", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun fetchRoute(origin: LatLng, destination: LatLng) {
        val originStr = "${origin.latitude},${origin.longitude}"
        val destinationStr = "${destination.latitude},${destination.longitude}"
        val apiKey = resources.getString(R.string.mapkey)

        RetrofitBuilder.retrofitForGoogle.getDirections(originStr, destinationStr, apiKey)
            .enqueue(object : Callback<DirectionsResponse> {
                override fun onResponse(
                    call: Call<DirectionsResponse>,
                    response: Response<DirectionsResponse>,
                ) {
                    if (response.isSuccessful) {
                        val directionsResponse = response.body()
                        if (directionsResponse != null) {
                            val points =
                                directionsResponse.routes.firstOrNull()?.overview_polyline?.points
                            if (points != null) {
                                drawPolyline(points)
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    // Handle the error
                }
            })
    }

    private fun drawPolyline(encodedPoints: String) {
        val decodedPath = PolyUtil.decode(encodedPoints)
        googleMap.addPolyline(
            PolylineOptions().addAll(decodedPath)
                .color(ContextCompat.getColor(this, R.color.colorPrimary))
        )
    }
}
