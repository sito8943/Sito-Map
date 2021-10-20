package com.sito.mapview

import com.mapbox.geojson.Point

data class SPoint(val lon: Double, val lat: Double, val name: String) {
    fun getAsPoint(): Point {
        return Point.fromLngLat(lon,lat)
    }
}