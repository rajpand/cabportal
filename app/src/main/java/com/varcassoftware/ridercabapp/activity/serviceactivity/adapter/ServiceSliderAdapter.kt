package com.varcassoftware.ridercabapp.activity.serviceactivity.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.varcassoftware.ridercabapp.R

class ServiceSliderAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<ServiceSliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_item_slider, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(imageList[position])
    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    class SliderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageViewServices)

        fun bind(imageResId: Int) {
            imageView.setImageResource(imageResId)
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        }
    }
}


