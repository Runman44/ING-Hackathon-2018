package nl.mranderson.hackathon2018.card

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class CardWithoutRule(val name: String,
                           val rule: String) : Parcelable