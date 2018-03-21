package nl.mranderson.hackathon2018.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Account(val iban: String,
                   val balance: Int) : Parcelable
