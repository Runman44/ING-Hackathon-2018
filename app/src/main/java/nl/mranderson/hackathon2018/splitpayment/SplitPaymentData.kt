package nl.mranderson.hackathon2018.splitpayment

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SplitPaymentData(val corporateAmount: String,
                            val personalAmount: String) : Parcelable
