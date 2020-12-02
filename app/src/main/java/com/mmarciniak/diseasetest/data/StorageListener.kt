package com.mmarciniak.diseasetest.data

interface StorageListener<T>  {
    fun readData(data: List<T>)
}