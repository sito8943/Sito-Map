package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.utils

import com.mapbox.geojson.Point

data class MapPoint(
    val text: String,
    val latitude: Double,
    val longitude: Double,
    val icon: String?,
) {

    fun getAsPoint(): Point {
        return Point.fromLngLat(longitude, latitude)
    }
}