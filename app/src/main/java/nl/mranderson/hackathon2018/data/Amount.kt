package nl.mranderson.hackathon2018.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Amount(val valueInCents: Int) : Parcelable