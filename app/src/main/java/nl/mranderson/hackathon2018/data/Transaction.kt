package nl.mranderson.hackathon2018.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Transaction(val card: Card,
                       val account: Account,
                       val amount: Amount) : Parcelable