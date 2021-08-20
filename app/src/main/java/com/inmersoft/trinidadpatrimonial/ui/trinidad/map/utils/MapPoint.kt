package com.inmersoft.trinidadpatrimonial.ui.trinidad.map.utils

import com.mapbox.geojson.Point

data class MapPoint(
    val placeIdInRoomDB: Int,
    val text: String,
    val latitude: Double,
    val longitude: Double,
    var icon: Int,
) {

    fun getAsPoint(): Point {
        return Point.fromLngLat(longitude, latitude)
    }
}