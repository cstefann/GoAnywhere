package com.example.goanywhere.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.goanywhere.R
import com.example.goanywhere.databinding.ItemTripInfoBinding
import com.example.goanywhere.models.Trip

class TripAdapter() : RecyclerView.Adapter<TripAdapter.TripViewHolder>(){
    inner class TripViewHolder(val binding: ItemTripInfoBinding) : RecyclerView.ViewHolder(binding.root)

     private var trip: List<Trip>? = null

     fun setData(trip: List<Trip>?) {
         this.trip = trip
     }

    override fun getItemCount(): Int {
        return trip?.size ?: 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripAdapter.TripViewHolder {
        return TripViewHolder(ItemTripInfoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        ))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TripAdapter.TripViewHolder, position: Int) {
        holder.binding.apply {
            val trip = trip?.get(position)
            if (trip != null) {
                nearestStation.text = "Nearest station: " + trip.nearestStation
                var routeName = trip.vehicul
                if (routeName.contains("bus_")){
                    routeName = routeName.replace("bus_", "Autobuzul ")
                }
                if (routeName.contains("tram_")){
                    routeName = routeName.replace("tram_", "Tramvaiul ")
                }
                vehicle.text = "Vehicle: $routeName"
                route.text = "Route: " + trip.ruta[0].statie.replace("""[\[\]]""".toRegex(), "")
                distance.text = "Distance: " + trip.distanta
                numberOfStops.text = "Number of Stops: " + trip.nrStatii
                arrivalTime.text = "Arrival time: " + trip.arrivalTime
            }
        }
        holder.binding.backButton.setOnClickListener { v ->
            Navigation.findNavController(v).navigate(R.id.action_tripFragment_to_routesFragment)
        }
    }
 }