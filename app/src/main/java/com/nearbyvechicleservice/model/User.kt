package com.nearbyvechicleservice.model

import android.os.Parcel
import android.os.Parcelable
import kotlin.contracts.contract

data class User(
    var id : String?,
    var fullName : String,
    var contact : String,
    var city : String,
    var email : String,
    var token : String
):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:"",
        parcel.readString()?:""
    )

    constructor():this("","","","","","")

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(fullName)
        parcel.writeString(contact)
        parcel.writeString(city)
        parcel.writeString(email)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }

}
