package com.example.cwb

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CWBData() : Parcelable {

    @SerializedName("startTime")
    @Expose
    var startTime: String = ""

    @SerializedName("endTime")
    @Expose
    var endTime: String = ""

    @SerializedName("parameter")
    @Expose
    var parameter: CWBParameter? = null

    constructor(parcel: Parcel) : this() {
        startTime = parcel.readString()!!
        endTime = parcel.readString()!!
        parameter = parcel.readParcelable(CWBParameter::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(startTime)
        parcel.writeString(endTime)
        parcel.writeParcelable(parameter, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CWBData> {
        override fun createFromParcel(parcel: Parcel): CWBData {
            return CWBData(parcel)
        }

        override fun newArray(size: Int): Array<CWBData?> {
            return arrayOfNulls(size)
        }
    }
}