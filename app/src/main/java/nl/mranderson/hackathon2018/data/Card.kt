package nl.mranderson.hackathon2018.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue

@Parcelize
data class Card(val iban: String,
                val accountId: String,
                val rules: @RawValue Rules) : Parcelable
