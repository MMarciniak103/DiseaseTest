package com.mmarciniak.diseasetest.data

import android.os.Parcel
import android.os.Parcelable

data class UserScore(val userName: String, val diseaseName: String,val score: Double,val date: String) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readDouble(),
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(userName)
        parcel.writeString(diseaseName)
        parcel.writeDouble(score)
        parcel.writeString(date)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserScore> {
        override fun createFromParcel(parcel: Parcel): UserScore {
            return UserScore(parcel)
        }

        override fun newArray(size: Int): Array<UserScore?> {
            return arrayOfNulls(size)
        }
    }
}