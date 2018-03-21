package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_split_payment_screen.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Transaction
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

        val transaction = intent.extras.get(KEY_TRANSACTION) as Transaction

        val payment = SplitPaymentData("5.00", "2.00")

        populateAmounts(transaction, payment)
    }

    private fun populateAmounts(transaction: Transaction, amountData: SplitPaymentData?) {
        amountData?.let {
            total_amt.text = "${transaction.amount.currency} ${(transaction.amount.valueInCents / 100.0f)}"
            corporate_amt_value.text = getString(R.string.amount_value, it.corporateAmount)
            personal_amt_value.text = getString(R.string.amount_value, it.personalAmount)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction) = Intent(context, PaymentResult::class.java).also {
            it.putExtra(KEY_TRANSACTION, transaction)
        }
    }
}
