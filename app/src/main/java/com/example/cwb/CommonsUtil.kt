package com.example.cwb

import android.os.Parcel
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import java.io.Reader

inline fun <reified T> Parcel.readValue(): T? {
    return readValue(T::class.java.classLoader) as T?
}

inline fun <reified T> GsonBuilder.registerTypeAdapter(adapter: TypeAdapter<T>): GsonBuilder {
    return registerTypeAdapter(T::class.java, adapter)
}

inline fun <reified T> Gson.fromJson(json: String): T {
    return fromJson(json, T::class.java)
}

inline fun <reified T> Gson.fromJson(json: Reader): T {
    return fromJson(json, T::class.java)
}

inline fun <reified T> Gson.fromJson(json: JsonElement): T {
    return fromJson(json, T::class.java)
}

inline fun <reified T> Gson.fromJson(reader: JsonReader): T {
    return fromJson(reader, T::class.java)
}