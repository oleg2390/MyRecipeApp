package com.example.myrecipeapp.data

import androidx.room.TypeConverter
import com.example.myrecipeapp.model.Ingredient
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json


class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromIngredients(value: List<Ingredient>): String =
        json.encodeToString(value)

    @TypeConverter
    fun toIngredients(value: String): List<Ingredient> =
        json.decodeFromString(value)

    @TypeConverter
    fun fromMethod(value: List<String>): String =
        json.encodeToString(value)

    @TypeConverter
    fun toMethod(value: String): List<String> =
        json.decodeFromString(value)
}