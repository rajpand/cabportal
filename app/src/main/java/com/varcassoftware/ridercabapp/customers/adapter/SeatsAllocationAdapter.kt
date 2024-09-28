package com.varcassoftware.ridercabapp.customers.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.varcassoftware.ridercabapp.R
import com.varcassoftware.ridercabapp.customers.models.Seat
import com.varcassoftware.ridercabapp.databinding.ItemSeatBinding


class SeatsAllocationAdapter(private var seats: List<Seat>) : RecyclerView.Adapter<SeatsAllocationAdapter.SeatViewHolder>() {

    inner class SeatViewHolder(val binding: ItemSeatBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeatViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemSeatBinding = DataBindingUtil.inflate(layoutInflater, R.layout.item_seat, parent, false)
        return SeatViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SeatViewHolder, position: Int) {
        val seat = seats[position]
        holder.binding.seat = seat

        // Optional: Handle seat click for booking
        holder.binding.root.setOnClickListener {
            if (!seat.isBooked) {
                seat.isBooked = true
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount() = seats.size

    // Method to update the seat list
    fun updateSeats(newSeats: List<Seat>) {
        seats = newSeats
        notifyDataSetChanged()
    }
}
