package com.varcassoftware.ridercabapp.customers

import android.app.ActivityOptions
import android.content.Intent
import android.location.Geocoder
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ListPopupWindow
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.customers.activity.SeatsAllocationActivity
import com.varcassoftware.ridercabapp.customers.adapter.AddressArrayAdapter
import com.varcassoftware.ridercabapp.customers.adapter.TravelingServicesAdapter
import com.varcassoftware.ridercabapp.customers.models.TravelingAddressSuggestion
import com.varcassoftware.ridercabapp.customers.viewmodels.TravelingServicesViewModels
import com.varcassoftware.ridercabapp.databinding.ActivityTravelingServicesBinding
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class TravelingServicesActivity : AppCompatActivity() , TravelingServicesAdapter.OnItemClickListener {

    private lateinit var binding: ActivityTravelingServicesBinding
    private lateinit var travelingServicesViewModels: TravelingServicesViewModels
    private lateinit var travelingServicesAdapter: TravelingServicesAdapter

    private lateinit var geocoder: Geocoder
    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteSessionToken: AutocompleteSessionToken
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var listPopupWindow: ListPopupWindow


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTravelingServicesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        travelingServicesViewModels = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[TravelingServicesViewModels::class.java]
        binding.tsViewModels = travelingServicesViewModels
        binding.lifecycleOwner = this

        val toolbar: Toolbar = binding.toolbarCustom.customToolbar
        setSupportActionBar(toolbar)
        loadMap()
       // loadRecycler()
        setObserve()
        setClcikListeners()
        // Observe suggested addresses


    }

    private fun setClcikListeners() {

        binding.edToLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //travelingServicesViewModels.fetchAddressSuggestions(s.toString())
                fetchAddressSuggestions(s.toString())
            }
        })

        binding.edFromLocation.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
               // travelingServicesViewModels.fetchAddressSuggestions(s.toString())
            }
        })

        binding.loginButton.setOnClickListener {
            loadRecycler()

        }
    }

    private fun setObserve() {
        travelingServicesViewModels.suggestedAddresses.observe(this) { suggestions ->

        }

    }

    private fun loadMap() {
        // Initialize Geocoder
        geocoder = Geocoder(this)
        // Initialize Places API
        Places.initialize(this, getString(R.string.mapkey))
        placesClient = Places.createClient(this)
        // Initialize Autocomplete Session Token
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()
        // Initialize Fused Location Provider Client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    private fun loadRecycler() {
        binding.recyclerViewForSearchDis.layoutManager = LinearLayoutManager(this)
        travelingServicesAdapter = TravelingServicesAdapter(emptyList(), this)
        binding.recyclerViewForSearchDis.adapter = travelingServicesAdapter


    }

    // Handle the item click
    override fun onItemClick(address: String) {
        Toast.makeText(this, "This Future Is Coming Soon", Toast.LENGTH_SHORT).show()
       /* val intent = Intent(this, SeatsAllocationActivity::class.java
        ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val options = ActivityOptions.makeCustomAnimation(
            this, R.anim.activity_fade_in, R.anim.activity_fade_out
        )
        startActivity(intent, options.toBundle())*/

    }

    private fun fetchAddressSuggestions(query: String) {
        val request = FindAutocompletePredictionsRequest.builder()
            .setSessionToken(autocompleteSessionToken)
            .setQuery(query)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            val predictionIds = response.autocompletePredictions.map { it.placeId }
            fetchPlaceDetails(predictionIds)
        }.addOnFailureListener { exception ->
            Toast.makeText(this@TravelingServicesActivity, "Error fetching suggestions: ${exception.message}", Toast.LENGTH_LONG).show()
        }
    }
    private fun fetchPlaceDetails(predictionIds: List<String>) {
        val suggestions = mutableListOf<TravelingAddressSuggestion>()
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)
        predictionIds.forEach { placeId ->
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                val suggestion = TravelingAddressSuggestion(
                    place.name ?: "",
                    place.address ?: "",
                    place.latLng?.latitude ?: 0.0,
                    place.latLng?.longitude ?: 0.0
                )
                suggestions.add(suggestion)
                showSuggestionPopup(suggestions, binding.edToLocation)
            }.addOnFailureListener { exception ->
                Toast.makeText(this@TravelingServicesActivity, "Error fetching place details: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showSuggestionPopup(suggestions: List<TravelingAddressSuggestion>, editText: EditText) {
        val adapter = AddressArrayAdapter(this, suggestions)
        listPopupWindow = ListPopupWindow(this)
        listPopupWindow.anchorView = editText
        listPopupWindow.setAdapter(adapter)
        listPopupWindow.setOnItemClickListener { _, _, position, _ ->
            val selectedSuggestion = suggestions[position]
            editText.setText(selectedSuggestion.name)
            listPopupWindow.dismiss()
            // Handle the selected suggestion as needed
        }
        listPopupWindow.show()
    }
}