package com.example.trendingrepositories.data.model

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class BuiltByConverters {
    @TypeConverter
    fun objectToString(stat: List<BuiltBy>): String {
        return Gson().toJson(stat)
    }
    @TypeConverter
    fun stringToListOfStrings(value: String): ArrayList<BuiltBy> {
        val listType: Type = object : TypeToken<ArrayList<BuiltBy?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}

