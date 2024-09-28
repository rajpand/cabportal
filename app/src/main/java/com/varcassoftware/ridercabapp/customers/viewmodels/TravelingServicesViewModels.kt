package com.varcassoftware.ridercabapp.customers.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.varcassoftware.ridercabapp.repository.Repository

class TravelingServicesViewModels(val repository: Repository) : ViewModel() {
    private val _suggestedAddresses = MutableLiveData<List<String>>()
    val suggestedAddresses: LiveData<List<String>> get() = _suggestedAddresses

    fun fetchAddressSuggestions(query: String) {
        _suggestedAddresses.value = listOf(
            "123 Main St, Springfield",
            "456 Elm St, Shelbyville",
            "789 Oak St, Capital City"
        ).filter { it.contains(query, ignoreCase = true) }
    }
}
