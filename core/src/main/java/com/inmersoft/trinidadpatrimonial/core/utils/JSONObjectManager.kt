package com.inmersoft.trinidadpatrimonial.core.utils

import org.json.JSONArray
import org.json.JSONObject

object JSONObjectManager {


    fun extractPlacesFromJSON(strJSON: String): List<JSONObject> {
        val placesList = mutableListOf<JSONObject>()
        val onlyPlaces = extractObjectFromJSON(strJSON, "places")
        for (aPlace in onlyPlaces) {
            placesList.add(aPlace)
        }
        return placesList
    }

    fun extractRoutesFromJSON(strJSON: String): List<JSONObject> {
        val routesList = mutableListOf<JSONObject>()
        val onlyRoutes = extractObjectFromJSON(strJSON, "routes")
        for (aRoute in onlyRoutes) {
            routesList.add(aRoute)
        }
        return routesList
    }

    fun extractPlacesTypeFromJSON(strJSON: String): List<JSONObject> {
        val placesTypeList = mutableListOf<JSONObject>()
        val onlyPlacesType = extractObjectFromJSON(strJSON, "place_type")
        for (aRoute in onlyPlacesType) {
            placesTypeList.add(aRoute)
        }
        return placesTypeList
    }

    fun extractPlaceAndPlacesTypeInObjectMap(placesList: List<JSONObject>): List<ObjectMap> {
        val mapPlaceTypeList = mutableListOf<ObjectMap>()
        placesList.forEach { currentPlace ->
            val jsonPlacesTypeArray = currentPlace.getJSONArray("place_type_id")
            val placesTypeList = jSONArrayToListInt(jsonPlacesTypeArray)
            val placeID = currentPlace.getInt("place_id")
            mapPlaceTypeList.add(ObjectMap(placeID, placesTypeList))
        }
        return mapPlaceTypeList
    }

    fun extractPlaceAndRoutesInObjectMap(placesList: List<JSONObject>): List<ObjectMap> {
        val mapRoutesIDList = mutableListOf<ObjectMap>()
        placesList.forEach { currentPlace ->
            val jsonRoutesIDArray = currentPlace.getJSONArray("routes_id")
            val placesTypeList = jSONArrayToListInt(jsonRoutesIDArray)
            val placeID = currentPlace.getInt("place_id")
            mapRoutesIDList.add(ObjectMap(placeID, placesTypeList))
        }
        return mapRoutesIDList
    }

    fun extractRoutesAndPlacesIDInObjectMap(routesList: List<JSONObject>): List<ObjectMap> {
        val mapRoutesIDList = mutableListOf<ObjectMap>()
        routesList.forEach { currentRoute ->
            val jsonPlacesIDArray = currentRoute.getJSONArray("places_id")
            val placesIDList = jSONArrayToListInt(jsonPlacesIDArray)
            val routeID = currentRoute.getInt("route_id")
            mapRoutesIDList.add(ObjectMap(routeID, placesIDList))
        }
        return mapRoutesIDList
    }

    fun extractPlaceTypeAndPlacesIDInObjectMap(placesType: List<JSONObject>): List<ObjectMap> {
        val mapPlaceTypeIDList = mutableListOf<ObjectMap>()
        placesType.forEach { currentPlaceType ->
            val jsonPlaceIDArray = currentPlaceType.getJSONArray("places_id")
            val placesIDList = jSONArrayToListInt(jsonPlaceIDArray)
            val placeTypeID = currentPlaceType.getInt("place_type_id")
            mapPlaceTypeIDList.add(ObjectMap(placeTypeID, placesIDList))
        }
        return mapPlaceTypeIDList
    }

    fun jSONArrayToListInt(jsonArray: JSONArray): List<Int> {
        val list = mutableListOf<Int>()
        (0 until jsonArray.length()).forEach { i ->
            list.add(jsonArray.getInt(i))
        }
        return list
    }

    fun extractObjectFromJSON(strJSON: String, objectName: String): List<JSONObject> {
        val objectList = mutableListOf<JSONObject>()
        val objectsJSON = JSONObject(strJSON).getJSONArray(objectName)
        (0 until objectsJSON.length()).mapTo(objectList) { objectsJSON.getJSONObject(it) }
        return objectList
    }

}