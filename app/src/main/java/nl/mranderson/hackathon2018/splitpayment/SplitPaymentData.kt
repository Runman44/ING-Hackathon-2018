package nl.mranderson.hackathon2018.splitpayment

import android.os.Parcel
import android.os.Parcelable

data class SplitPaymentData(val corporateAmount: String,
                            val personalAmount: String) : Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString()) {
    }

    override fun writeToParcel(dest: Parcel?, flags: Int) {
        dest?.writeString(corporateAmount)
        dest?.writeString(personalAmount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SplitPaymentData> {
        override fun createFromParcel(parcel: Parcel): SplitPaymentData {
            return SplitPaymentData(parcel)
        }

        override fun newArray(size: Int): Array<SplitPaymentData?> {
            return arrayOfNulls(size)
        }
    }
}
