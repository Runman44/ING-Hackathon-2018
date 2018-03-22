package nl.mranderson.hackathon2018.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Week(val monday: Boolean, val tuesday: Boolean, val wednesday: Boolean, val thursday: Boolean, val friday: Boolean, val saturday: Boolean, val sunday: Boolean) : Parcelable
