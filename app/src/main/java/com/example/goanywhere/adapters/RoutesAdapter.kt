package com.example.goanywhere.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.goanywhere.db.RouteModel
import com.example.goanywhere.databinding.ItemTripBinding

class RoutesAdapter : RecyclerView.Adapter<RoutesAdapter.RouteViewHolder>()  {
    inner class RouteViewHolder(val binding: ItemTripBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<RouteModel>() {
        override fun areItemsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: RouteModel, newItem: RouteModel): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var routes: List<RouteModel>
        get() = differ.currentList
        set(value) { differ.submitList(value) }

    override fun getItemCount() = routes.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteViewHolder {
        return RouteViewHolder(ItemTripBinding.inflate(
             LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RouteViewHolder, position: Int) {
        holder.binding.apply {
            val routes = routes[position]
            tvTitle.text = routes.id + ": " + routes.route
        }
    }
}