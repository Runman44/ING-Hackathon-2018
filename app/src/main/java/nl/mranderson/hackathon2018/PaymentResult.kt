package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_split_payment_screen.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.splitpayment.SplitPaymentData

class PaymentResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_payment_screen)

        initViews()
    }

    private fun initViews() {
        val cardFragment = CardImageFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.cardFragment, cardFragment)
                .commit()

        val payment = SplitPaymentData("5.00", "2.00")

        populateAmounts(payment)
    }

    private fun populateAmounts(amountData: SplitPaymentData?) {
        amountData?.let {
            corporate_amt.text = getString(R.string.amount_value, it.corporateAmount)
            personal_amt.text = getString(R.string.amount_value, it.personalAmount)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, PaymentResult::class.java)
    }
}
