package com.example.spotifiubyfy01.search

interface VolleyCallBack<T> {
    fun updateDataInRecyclerView(list: List<T>)
}