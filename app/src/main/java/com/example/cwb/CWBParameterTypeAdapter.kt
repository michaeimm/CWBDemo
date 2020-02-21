package com.example.cwb

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

class CWBParameterTypeAdapter: TypeAdapter<CWBParameter>() {
    override fun write(out: JsonWriter, value: CWBParameter) {
        out.apply {
            beginObject()

            name("parameterName").value(value.parameterName)
            name("parameterUnit").value(value.parameterUnit)

            endObject()
        }
    }

    override fun read(`in`: JsonReader): CWBParameter {
        val result = CWBParameter()

        `in`.apply {
            beginObject()
            while (hasNext()) {
                when (nextName()) {
                    "parameterName" -> result.parameterName = nextString()
                    "parameterUnit" -> result.parameterUnit = nextString()
                    else -> skipValue()
                }
            }
            endObject()
        }

        return result
    }
}