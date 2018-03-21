package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_active_payment.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Transaction

class ActivePayment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_payment)

        val fragment: CardImageFragment
        if (savedInstanceState == null) {
            fragment = CardImageFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.cardFragment, fragment, "CARDIMAGETAG")
                    .commit()
        }
    }

    override fun onResume() {
        super.onResume()

        val transaction = intent.getParcelableExtra<Transaction>(KEY_TRANSACTION);

        paymentTotal.text = "${transaction.amount.currency} ${(transaction.amount.valueInCents / 100.0f)}"

        Handler().postDelayed( {
            startActivity(PaymentResult.createIntent(this))
        }, 5000)

    }

    companion object {
        private val KEY_TRANSACTION = "transaction"

        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction) : Intent {
            return Intent(context, ActivePayment::class.java).also {
                it.putExtra(KEY_TRANSACTION, transaction)
            }
        }
    }
}
