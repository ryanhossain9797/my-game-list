//----------------Model for subentry (Ex: comment on game entries)

package com.example.mygamelist

import android.os.Parcel
import android.os.Parcelable


class SubEntryModel(var _id: String, var username: String, var comment:String) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_id)
        parcel.writeString(username)
        parcel.writeString(comment)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SubEntryModel> {
        override fun createFromParcel(parcel: Parcel): SubEntryModel {
            return SubEntryModel(parcel)
        }

        override fun newArray(size: Int): Array<SubEntryModel?> {
            return arrayOfNulls(size)
        }
    }
}