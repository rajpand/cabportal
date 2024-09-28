package com.varcassoftware.ridercabapp.customers.activity

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.customers.adapter.SeatsAllocationAdapter
import com.varcassoftware.ridercabapp.customers.models.BookingViewModel
import com.varcassoftware.ridercabapp.customers.viewmodels.TravelingServicesViewModels
import com.varcassoftware.ridercabapp.databinding.ActivitySeatsAllocationBinding
import com.varcassoftware.ridercabapp.repository.RepositoryClass
import com.varcassoftware.ridercabapp.viewModelFactory.ViewModelFactory

class SeatsAllocationActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySeatsAllocationBinding
    private lateinit var viewModel: BookingViewModel
    private lateinit var seatAdapter: SeatsAllocationAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Initialize data binding
        binding = ActivitySeatsAllocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(
            this, ViewModelFactory("", RepositoryClass())
        )[BookingViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        loadRecycler()
        setObserve()
        setClcikListeners()

        viewModel.selectVehicle("innova")
    }

    private fun setClcikListeners() {

    }

    private fun setObserve() {
        viewModel.seats.observe(this, { seats ->
            seatAdapter.updateSeats(seats)
        })
    }

    private fun loadRecycler() {
        seatAdapter = SeatsAllocationAdapter(emptyList()) // Initially empty list
        binding.recyclerViewSeats.adapter = seatAdapter
    }
}