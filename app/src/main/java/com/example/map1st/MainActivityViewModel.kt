package com.example.map1st

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.MarkerOptions

private const val TAG = "MainActivityViewModel"

class MainActivityViewModel : ViewModel() {
    private val markersLiveData: MutableLiveData<MutableList<GetLocation>>

    var classMaps = MapsActivity()
    var listMarkerInfo = classMaps.listMarkerInfo

    init {
        Log.i(TAG, "init")
        markersLiveData = MutableLiveData()
        markersLiveData.value = displayMarker()
    }

    fun getMarkers(): LiveData<MutableList<GetLocation>> {
        return markersLiveData
    }


    fun displayMarker(): MutableList<GetLocation> {
        var newList = mutableListOf<GetLocation>()
        for (marker in classMaps.listMarkerInfo) {
            newList.add(marker)
            classMaps.mMap.addMarker(MarkerOptions().position(marker.coordinates).title(marker.title).draggable(false))
        }

        return newList
    }

}