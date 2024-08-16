package com.varcassoftware.ridercabapp.activity.serviceactivity.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.databinding.OneViewForServicesBinding
import com.varcassoftware.ridercabapp.entity.ServiceItem

class ServiceAdapter(
    private val serviceList: List<ServiceItem>,
    private val listener: OnServiceItemClickListener
) : RecyclerView.Adapter<ServiceAdapter.ServiceViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServiceViewHolder {
        val binding = OneViewForServicesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ServiceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ServiceViewHolder, position: Int) {
        val serviceItem = serviceList[position]
        holder.bind(serviceItem, listener)
    }

    override fun getItemCount(): Int {
        return serviceList.size
    }

    class ServiceViewHolder(private val binding: OneViewForServicesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(serviceItem: ServiceItem, listener: OnServiceItemClickListener) {
            binding.serviceTitle.text = serviceItem.title
            binding.serviceImage.setImageResource(serviceItem.imageUrl)
            try {
                Picasso.get()
                    .load(serviceItem.imageUrl)
                    .resize(binding.serviceImage.width, binding.serviceImage.height)
                    .error(R.drawable.services_slider_1)
                    .into(binding.serviceImage)
            }catch (e:Exception){
                Picasso.get()
                Picasso.get()
                    .load(serviceItem.imageUrl)
                    .resize(100,100)
                    .error(R.drawable.services_slider_1)
                    .into(binding.serviceImage)
            }
            binding.root.setOnClickListener {
                listener.onServiceItemClick(serviceItem)
            }
        }
    }
}

interface OnServiceItemClickListener {
    fun onServiceItemClick(serviceItem: ServiceItem)
}
