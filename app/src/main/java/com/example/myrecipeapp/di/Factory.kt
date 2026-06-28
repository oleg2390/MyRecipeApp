package com.example.myrecipeapp.di

interface Factory<T> {
    fun create(): T
}