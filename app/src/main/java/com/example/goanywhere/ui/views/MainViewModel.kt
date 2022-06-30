package com.example.goanywhere.ui.views

import androidx.lifecycle.ViewModel
import com.example.goanywhere.models.Coordinates
import com.example.goanywhere.models.RouteModel
import com.example.goanywhere.models.Trip
import com.example.goanywhere.network.RetroRepository
import com.example.goanywhere.shareable.coordinatesData
import com.example.goanywhere.shareable.getRouteReqData
import com.example.goanywhere.shareable.infoRouteData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: RetroRepository
): ViewModel() {
    fun getTrip(): List<Trip> {
        return repository.getTripFromDB()
    }

    fun makeProvideCoordsApiCall(){
        repository.provideCoordsApiCall(coordinatesData.location)
    }

    suspend fun makeProvideLastStatusApiCall(): Coordinates {
        return repository.provideLastStatusApiCall(coordinatesData.location)
    }

    suspend fun makeGetRouteApiCall(): List<RouteModel>{
        return repository.getRoutesApiCall(
            getRouteReqData.latitudeStart.toString(),
            getRouteReqData.longitudeStart.toString(),
            getRouteReqData.latitudeEnd.toString(),
            getRouteReqData.longitudeEnd.toString()
        )
    }

    fun makeInfoApiCall() {
        repository.routeInfoApiCall(
            getRouteReqData.latitudeStart.toString(),
            getRouteReqData.longitudeStart.toString(),
            getRouteReqData.latitudeEnd.toString(),
            getRouteReqData.longitudeEnd.toString(),
            infoRouteData.routeName,
            infoRouteData.routeNumber.toString()
        )
    }
}