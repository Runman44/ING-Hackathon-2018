package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_split_payment_screen.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Transaction

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

        populateAmounts(transaction)
    }

    private fun populateAmounts(transaction: Transaction) {
        transaction.let {
            total_amt.text = "${it.amount.currency} ${(it.amount.valueInCents / 100.0f)}"

            val corporateAmount = transaction.card.rules.amount

            val personalAmount = it.amount.valueInCents - corporateAmount.valueInCents

            corporate_amt_value.text = "${corporateAmount.currency} ${(corporateAmount.valueInCents / 100.00f)}"
            personal_amt_value.text = if (personalAmount > 0) {
                "${corporateAmount.currency} ${(personalAmount / 100.00f)}"
            } else getString(R.string.amount_value, "0.00")
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction) = Intent(context, PaymentResult::class.java).also {
            it.putExtra(KEY_TRANSACTION, transaction)
        }
    }
}
