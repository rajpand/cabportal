package com.varcassoftware.ridercabapp.customers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varcassoftware.ridercabapp.databinding.OneViewForvehicaldetailsBinding

class TravelingServicesAdapter(
    private val addressList: List<String>,
    private val onItemClickListener: OnItemClickListener
) : RecyclerView.Adapter<TravelingServicesAdapter.AddressViewHolder>() {

    inner class AddressViewHolder(private val binding: OneViewForvehicaldetailsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener.onItemClick("gffsfxjf")
                }
            }
        }

        fun bind(address: String) {
            //binding.vehicalName.text = address
            //binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = OneViewForvehicaldetailsBinding.inflate(inflater, parent, false)
        return AddressViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {
       // holder.bind(addressList[position])
    }

    override fun getItemCount() = 10

    interface OnItemClickListener {
        fun onItemClick(address: String)


    }
}
