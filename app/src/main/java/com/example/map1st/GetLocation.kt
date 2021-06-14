package com.example.map1st

import com.google.android.gms.maps.model.LatLng
import java.io.Serializable


data class GetLocation(var title: String, var coordinates: LatLng) : Serializable
//, var colour: Float