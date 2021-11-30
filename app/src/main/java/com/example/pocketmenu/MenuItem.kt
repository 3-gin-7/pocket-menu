package com.example.pocketmenu

import android.os.Parcel
import android.os.Parcelable

data class MenuItem(
    val itemName: String? ="",
    val price: Double = 0.0,
    val category: String? ="",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(itemName)
        parcel.writeDouble(price)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<MenuItem> {
        override fun createFromParcel(parcel: Parcel): MenuItem {
            return MenuItem(parcel)
        }

        override fun newArray(size: Int): Array<MenuItem?> {
            return arrayOfNulls(size)
        }
    }
}