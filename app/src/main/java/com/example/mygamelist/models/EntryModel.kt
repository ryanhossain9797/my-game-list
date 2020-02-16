package com.example.mygamelist.models

import android.os.Parcel
import android.os.Parcelable
class EntryModel (var _id: String, var title:String, var content:String, var imgurl:String) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(imgurl)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<EntryModel> {
        override fun createFromParcel(parcel: Parcel): EntryModel {
            return EntryModel(parcel)
        }

        override fun newArray(size: Int): Array<EntryModel?> {
            return arrayOfNulls(size)
        }
    }

}