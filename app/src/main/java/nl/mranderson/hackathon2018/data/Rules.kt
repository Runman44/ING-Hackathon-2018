package nl.mranderson.hackathon2018.data

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@SuppressLint("ParcelCreator")
@Parcelize
data class Rules(val accountId:String, val name: String, val accumulate : Boolean, val initAmount: Amount, val amount: Amount, val week : Week) : Parcelable
