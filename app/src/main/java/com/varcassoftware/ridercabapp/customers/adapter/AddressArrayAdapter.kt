package com.varcassoftware.ridercabapp.customers.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.varcassoftware.ridercabapp.customers.models.TravelingAddressSuggestion

class AddressArrayAdapter(
    context: Context,
    private val suggestions: List<TravelingAddressSuggestion>
) : ArrayAdapter<TravelingAddressSuggestion>(context, 0, suggestions) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(
            android.R.layout.simple_dropdown_item_1line, parent, false
        )
        val suggestion = getItem(position)
        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = suggestion?.name ?: ""
        return view
    }
}
