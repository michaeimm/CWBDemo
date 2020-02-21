package com.example.cwb

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class CWBDataTypeAdapter: TypeAdapter<CWBData>() {


    override fun write(out: JsonWriter, value: CWBData) {
        val parameterTypeAdapter = CWBParameterTypeAdapter()
        out.apply {
            beginObject()

            name("startTime").value(value.startTime)
            name("endTime").value(value.endTime)
            parameterTypeAdapter.write(out, value.parameter!!)

            endObject()
        }
    }

    override fun read(`in`: JsonReader): CWBData {
        val result = CWBData()

        `in`.apply {
            beginObject()
            while (hasNext()) {
                when (nextName()) {
                    "startTime" -> result.startTime = nextString()
                    "endTime" -> result.endTime = nextString()
                    "parameter" -> {
                        val parameterTypeAdapter = CWBParameterTypeAdapter()
                        result.parameter = parameterTypeAdapter.read(`in`)
                    }
                    else -> skipValue()
                }
            }
            endObject()
        }

        return result
    }


}