package com.example.goanywhere.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.Navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.goanywhere.R
import com.example.goanywhere.databinding.ItemTripBinding
import com.example.goanywhere.models.RouteModel
import com.example.goanywhere.shareable.getRouteReqData
import com.example.goanywhere.shareable.infoRouteData

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
            tvTitle.text = " " + routes.route
            if (getRouteReqData.latitudeEnd == 0.0 && getRouteReqData.longitudeEnd == 0.0){
                expandButton.isVisible = false
            }
        }
        holder.binding.expandButton.setOnClickListener { v ->
            infoRouteData.routeNumber = routes[position].id.toInt()
            var routeName = routes[position].route
            if (routeName.contains("Autobuzul ")){
                routeName = routeName.replace("Autobuzul ", "bus_")
            }
            if (routeName.contains("Tramvai ")){
                routeName = routeName.replace("Tramvai ", "tram_")
            }

            infoRouteData.routeName = routeName
            findNavController(v).navigate(R.id.action_routesFragment_to_tripFragment)
        }
    }
}
