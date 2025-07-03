package com.example.myrecipeapp.data

import android.content.Context
import com.example.myrecipeapp.R

class AppPreferences(private val context: Context) {

    fun saveFavorites(favoriteId: Set<String>) {
        val sharedPrefs = context.getSharedPreferences(
            context.getString(R.string.favorite),
            Context.MODE_PRIVATE
        )
        sharedPrefs.edit()
            .putStringSet(context.getString(R.string.favorite_recipe), favoriteId)
            .apply()
    }

    fun getFavorites(): MutableSet<String> {
        val sharedPrefs = context.getSharedPreferences(
            context.getString(R.string.favorite),
            Context.MODE_PRIVATE
        )
        val storedSet =
            sharedPrefs.getStringSet(context.getString(R.string.favorite_recipe), emptySet())
        return HashSet(storedSet ?: emptySet())
    }
}