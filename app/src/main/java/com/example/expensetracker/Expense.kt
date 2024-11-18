package com.example.expensetracker

import android.os.Parcel
import android.os.Parcelable

data class Expense(
    val id: String,
    val title: String,
    val category: String,
    val amount: Double,
    val date: String,
    val currency: String
):Parcelable {
    constructor(parcel: Parcel) : this(
        id = parcel.readString() ?: "",
        title = parcel.readString() ?: "",
        amount = parcel.readDouble(),
        category = parcel.readString() ?: "",
        date = parcel.readString() ?: "",
        currency = parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(category)
        parcel.writeDouble(amount)
        parcel.writeString(date)
        parcel.writeString(currency)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Expense> {
        override fun createFromParcel(parcel: Parcel): Expense {
            return Expense(parcel)
        }

        override fun newArray(size: Int): Array<Expense?> {
            return arrayOfNulls(size)
        }
    }
}
