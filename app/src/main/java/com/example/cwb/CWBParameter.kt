package com.example.cwb

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CWBParameter() : Parcelable {

    @SerializedName("parameterName")
    @Expose
    var parameterName: String = ""

    @SerializedName("parameterUnit")
    @Expose
    var parameterUnit: String = ""

    constructor(parcel: Parcel): this() {
        parameterName = parcel.readValue()!!
        parameterUnit = parcel.readValue()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.apply {
            writeValue(parameterName)
            writeValue(parameterUnit)
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CWBParameter> {
        override fun createFromParcel(parcel: Parcel): CWBParameter {
            return CWBParameter(parcel)
        }

        override fun newArray(size: Int): Array<CWBParameter?> {
            return arrayOfNulls(size)
        }
    }
}