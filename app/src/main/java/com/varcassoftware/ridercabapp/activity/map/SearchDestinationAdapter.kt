package com.varcassoftware.ridercabapp.activity.map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.varcassoftware.ridercabapp.activity.map.fragment.BottomSheetDialog.OnClickButtonListener
import com.varcassoftware.ridercabapp.databinding.OneViewForSearchlistBinding


class SearchDestinationAdapter : RecyclerView.Adapter<SearchDestinationAdapter.ViewHolder>() {
private  var listener: OnClickItemListener?=null
    private val data = listOf("Item 1", "Item 2", "Item 3") // Sample data

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = OneViewForSearchlistBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(data[position])
        holder.itemView.setOnClickListener {
            listener?.onItemClicked(data[position])
        }
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder(private val binding: OneViewForSearchlistBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: String) {
            binding.previousDestination.text = item
        }
    }

    interface  OnClickItemListener{
        fun  onItemClicked(forWhat:String)
    }
}
