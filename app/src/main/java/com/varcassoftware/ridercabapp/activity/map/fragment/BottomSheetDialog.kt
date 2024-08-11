package com.varcassoftware.ridercabapp.activity.map.fragment

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.FrameLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.activity.map.SearchDestinationAdapter
import com.varcassoftware.ridercabapp.activity.map.adapter.RecyclerViewForSearchDis
import com.varcassoftware.ridercabapp.activity.map.entity.AddressSuggestion
import com.varcassoftware.ridercabapp.databinding.BottomSheetLayoutBinding
import java.io.IOException

class BottomSheetDialog : BottomSheetDialogFragment(),
    SearchDestinationAdapter.OnClickItemListener {
    private lateinit var geocoder: Geocoder
    private var _binding: BottomSheetLayoutBinding? = null
    private val binding get() = _binding!!
    private var listener: OnClickButtonListener? = null
    private lateinit var destinationLocation: LatLng
    private lateinit var placesClient: PlacesClient
    private lateinit var autocompleteSessionToken: AutocompleteSessionToken
    private lateinit var recyclerViewForSearchDis: RecyclerViewForSearchDis

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setClickListeners()
        setupRecyclerView()
        setupAutocomplete()
    }

    private fun setClickListeners() {
        binding.searchDestination.setOnClickListener {
            //listener?.onClickButtonClickedOn("1SD", "hello dear")
            //dismiss()
            fullScreenView()
        }
    }

    private fun setupAutocomplete() {
        binding.endLocation.apply {
            isIconifiedByDefault = false
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let {
                        if (it.isNotEmpty()) {
                            loadAddressFromMap(it)
                        }
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let {
                        if (it.isNotEmpty()) {
                            fetchAddressSuggestions(it)
                        }
                    }
                    return true
                }
            })
        }
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
            Toast.makeText(activity, "Error fetching suggestions: ${exception.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun fetchPlaceDetails(predictionIds: List<String>) {
        val placeFields = listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

        predictionIds.forEach { placeId ->
            val request = FetchPlaceRequest.builder(placeId, placeFields).build()
            placesClient.fetchPlace(request).addOnSuccessListener { response ->
                val place = response.place
                val suggestion = AddressSuggestion(
                    place.name ?: "",
                    place.address ?: "",
                    place.latLng?.latitude ?: 0.0,
                    place.latLng?.longitude ?: 0.0
                )
                updateSearchSuggestions(listOf(suggestion))
            }.addOnFailureListener { exception ->
                Toast.makeText(activity, "Error fetching place details: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }


    private fun updateSearchSuggestions(suggestions: List<AddressSuggestion>) {
        val recyclerView: RecyclerView = binding.recyclerViewForSearchDis
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerViewForSearchDis = RecyclerViewForSearchDis(
            suggestions,
            object : RecyclerViewForSearchDis.OnItemClickListener {
                override fun onItemClick(address: String) {
                    loadAddressFromMap(address)
                    recyclerView.visibility = View.GONE
                }
            })
        recyclerView.adapter = recyclerViewForSearchDis
        recyclerView.visibility = View.VISIBLE
    }

    private fun setupRecyclerView() {
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = SearchDestinationAdapter() // Replace with your actual adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        activity?.findViewById<Button>(R.id.conformLocationButton)?.visibility = View.VISIBLE
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnClickButtonListener
        geocoder = activity?.let { Geocoder(it) }!!
        activity?.let { Places.initialize(it, resources.getString(R.string.mapkey)) }
        placesClient = activity?.let { Places.createClient(it) }!!
        autocompleteSessionToken = AutocompleteSessionToken.newInstance()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState) as BottomSheetDialog
        val bottomSheet =
            dialog.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
        bottomSheet?.let {
            val behavior = BottomSheetBehavior.from(it)
            behavior.peekHeight = it.height
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    Toast.makeText(context, "onSlide", Toast.LENGTH_SHORT).show()
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            Toast.makeText(context, "STATE_HIDDEN", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_EXPANDED -> {
                            Toast.makeText(context, "STATE_EXPANDED", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            Toast.makeText(context, "STATE_COLLAPSED", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_DRAGGING -> {
                            Toast.makeText(context, "STATE_DRAGGING", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_SETTLING -> {
                            Toast.makeText(context, "STATE_SETTLING", Toast.LENGTH_SHORT).show()
                        }

                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            Toast.makeText(context, "STATE_HALF_EXPANDED", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            })
        }

        return dialog
    }


    override fun onStart() {
        super.onStart()
        dialog?.let {
            val bottomSheet =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            val behavior = BottomSheetBehavior.from(bottomSheet!!)
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }


    private fun fullScreenView() {
        dialog?.let {
            val bottomSheet =
                it.findViewById<FrameLayout>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { bs ->
                val layoutParams = bs.layoutParams
                layoutParams.height =
                    if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    } else {
                        ViewGroup.LayoutParams.MATCH_PARENT

                    }

                if (layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    binding.rlSearchDesitination.visibility = View.VISIBLE
                    binding.cdDestination.visibility = View.GONE
                } else {
                    binding.rlSearchDesitination.visibility = View.GONE
                    binding.cdDestination.visibility = View.VISIBLE
                }
                bs.layoutParams = layoutParams
                val behavior = BottomSheetBehavior.from(bs)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
    }


    private fun getScreenHeight(): Int {
        return resources.displayMetrics.heightPixels
    }

    interface OnClickButtonListener {
        fun onClickButtonClickedOn(forWhat: String, massage: String)
    }

    override fun onItemClicked(forWhat: String) {
        dismiss()
        listener?.onClickButtonClickedOn("address", forWhat)
    }

    private fun loadAddressFromMap(query: String) {
        try {
            val addresses = geocoder.getFromLocationName(query, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                val addressText = address.getAddressLine(0) // Full address
                destinationLocation = LatLng(address.latitude, address.longitude)
            }
        } catch (e: IOException) {
            Toast.makeText(activity, "Geocoder service not available", Toast.LENGTH_LONG).show()
        }
    }
}
