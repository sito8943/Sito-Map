package com.inmersoft.trinidadpatrimonial.database.data.converters

import androidx.room.TypeConverter
import com.inmersoft.trinidadpatrimonial.database.data.entity.*
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import java.lang.reflect.Type


class Converters {
    @TypeConverter
    fun stringListToString(list: List<String>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun stringToStringList(json: String): List<String>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, String::class.java)
        val adapter: JsonAdapter<List<String>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun stringToRouteTranslationList(json: String): List<RouteTranslation>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, RouteTranslation::class.java)
        val adapter: JsonAdapter<List<RouteTranslation>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun stringToPlaceTranslationList(json: String): List<PlaceTranslation>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, PlaceTranslation::class.java)
        val adapter: JsonAdapter<List<PlaceTranslation>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun stringToPlaceList(json: String): List<Place>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Place::class.java)
        val adapter: JsonAdapter<List<Place>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun stringToRouteList(json: String): List<Route>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Route::class.java)
        val adapter: JsonAdapter<List<Route>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun stringToPlaceTypeList(json: String): List<PlaceType>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, PlaceType::class.java)
        val adapter: JsonAdapter<List<PlaceType>> = moshi.adapter(type)
        return adapter.fromJson(json)
    }

    @TypeConverter
    fun routeTranslationListToString(list: List<RouteTranslation>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, RouteTranslation::class.java)
        val adapter: JsonAdapter<List<RouteTranslation>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun placeTranslationListToString(list: List<PlaceTranslation>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, PlaceTranslation::class.java)
        val adapter: JsonAdapter<List<PlaceTranslation>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun placeTypeListToString(list: List<PlaceType>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, PlaceType::class.java)
        val adapter: JsonAdapter<List<PlaceType>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun placeListToString(list: List<Place>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Place::class.java)
        val adapter: JsonAdapter<List<Place>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun routeListToString(list: List<Route>?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Route::class.java)
        val adapter: JsonAdapter<List<Route>> = moshi.adapter(type)
        return adapter.toJson(list)
    }

    @TypeConverter
    fun locationToString(location: Location?): String {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, Location::class.java)
        val adapter: JsonAdapter<Location> = moshi.adapter(type)
        return adapter.toJson(location)
    }

    @TypeConverter
    fun stringToLocation(json: String?): Location? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, Location::class.java)
        val adapter: JsonAdapter<Location> = moshi.adapter(type)
        return adapter.fromJson(json?:"")
    }


    @TypeConverter
    fun stringToTypeTranslation(json: String?): TypeTranslation? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, TypeTranslation::class.java)
        val adapter: JsonAdapter<TypeTranslation> = moshi.adapter(type)
        return adapter.fromJson(json?:"")
    }

    @TypeConverter
    fun typeTranslationToString(typeTranslation: TypeTranslation?): String? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, TypeTranslation::class.java)
        val adapter: JsonAdapter<TypeTranslation> = moshi.adapter(type)
        return adapter.toJson(typeTranslation)
    }

    @TypeConverter
    fun stringToTypeTranslationList(json: String?): List<TypeTranslation>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, TypeTranslation::class.java)
        val adapter: JsonAdapter<List<TypeTranslation>> = moshi.adapter(type)
        return adapter.fromJson(json?:"")
    }

    @TypeConverter
    fun typeTranslationListToString(typeTranslation: List<TypeTranslation>?): String? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, TypeTranslation::class.java)
        val adapter: JsonAdapter<List<TypeTranslation>> = moshi.adapter(type)
        return adapter.toJson(typeTranslation)
    }

    @TypeConverter
    fun stringToIntList(json: String?): List<Int>? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(type)
        return adapter.fromJson(json?:"")
    }

    @TypeConverter
    fun intListToString(intList: List<Int>?): String? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(List::class.java, Int::class.javaObjectType)
        val adapter: JsonAdapter<List<Int>> = moshi.adapter(type)
        return adapter.toJson(intList)
    }


    @TypeConverter
    fun stringToTrinidad(trinidad: String): Trinidad? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, Trinidad::class.java)
        val adapter: JsonAdapter<Trinidad> = moshi.adapter(type)
        return adapter.fromJson(trinidad)
    }

    @TypeConverter
    fun trinidadToString(trinidad: Trinidad?): String? {
        val moshi = Moshi.Builder().build()
        val type: Type = Types.newParameterizedType(String::class.java, Trinidad::class.java)
        val adapter: JsonAdapter<Trinidad> = moshi.adapter(type)
        return adapter.toJson(trinidad)
    }
}
