package com.varcassoftware.ridercabapp.activity.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.varcassoftware.ridercabapp.activity.map.entity.AddressSuggestion
import com.varcassoftware.ridercabapp.databinding.OneViewForAddresssuggestionBinding
import com.varcassoftware.ridercabapp.databinding.OneViewForSearchlistBinding


class RecyclerViewForSearchDis(private val suggestions: List<AddressSuggestion>, private val listener: OnItemClickListener)
    : RecyclerView.Adapter<RecyclerViewForSearchDis.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(address: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OneViewForAddresssuggestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(suggestions[position])

    }

    override fun getItemCount(): Int = suggestions.size

    inner class ViewHolder(private val binding: OneViewForAddresssuggestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: AddressSuggestion) {
            binding.locationName.text = item.name
            binding.locationAddress.text = item.address
        }
    }
}
