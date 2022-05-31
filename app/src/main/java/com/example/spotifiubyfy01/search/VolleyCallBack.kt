package com.example.spotifiubyfy01.search

interface VolleyCallBack<T> {
    fun updateData(list: List<T>)
}