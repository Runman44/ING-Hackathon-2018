package nl.mranderson.hackathon2018.data

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Transaction(val card: Card,
                       val account: Account,
                       val amount: Amount) : Parcelable