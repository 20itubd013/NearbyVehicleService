package com.nearbyvechicleservice.model

import android.os.Parcel
import android.os.Parcelable


data class Mechanic(
    var id : String?,
    var fullName : String,
    var contact : String,
    var city : String,
    var address : String,
    var lat : String,
    var lng : String,
    var garagename : String,
    var email : String,
    var token : String,

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"")
    constructor():this("","","","","","","","","","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fullName)
        parcel.writeString(contact)
        parcel.writeString(city)
        parcel.writeString(address)
        parcel.writeString(lat)
        parcel.writeString(lng)
        parcel.writeString(garagename)
        parcel.writeString(email)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Mechanic> {
        override fun createFromParcel(parcel: Parcel): Mechanic {
            return Mechanic(parcel)
        }

        override fun newArray(size: Int): Array<Mechanic?> {
            return arrayOfNulls(size)
        }
    }

}
