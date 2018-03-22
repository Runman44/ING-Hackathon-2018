package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_split_payment_screen.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Amount
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
            total_amt.text = getAmountString(it.amount)

            val corporateAmount = transaction.card.rules.amount

            val personalAmount = Amount(it.amount.valueInCents - corporateAmount.valueInCents)

            corporate_amt_value.text = getAmountString(corporateAmount)
            personal_amt_value.text = if (personalAmount.valueInCents > 0) getAmountString(personalAmount) else getString(R.string.amount_value, "0.00")
        }
    }

    private fun getAmountString(amount: Amount): String =
            getString(R.string.amount_value_dynamic, amount.currency, (amount.valueInCents / 100.0f))

    companion object {
        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction) = Intent(context, PaymentResult::class.java).also {
            it.putExtra(KEY_TRANSACTION, transaction)
        }
    }
}
