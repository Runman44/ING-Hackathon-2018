package nl.mranderson.hackathon2018.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Card(val iban: String,
                val accountId: String,
                val rules: Rules) : Parcelable



