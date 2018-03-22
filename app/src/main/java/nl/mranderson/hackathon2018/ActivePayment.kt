package nl.mranderson.hackathon2018

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_active_payment.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Amount
import nl.mranderson.hackathon2018.data.Transaction

const val KEY_TRANSACTION = "transaction"

const val PAYMENT_REQUEST_CODE = 6

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

    @SuppressLint("RestrictedApi")
    private fun showResults(transaction: Transaction) {
        val options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(this, paymentTotal, paymentTotal.transitionName)
        startActivityForResult(PaymentResult.createIntent(this, transaction), PAYMENT_REQUEST_CODE, options.toBundle())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK && intent != null) {
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResume() {
        super.onResume()
        val transaction = intent.getParcelableExtra<Transaction>(KEY_TRANSACTION);

        paymentTotal.text = getAmountString(transaction.amount)

        Handler().postDelayed({
            showResults(transaction)
        }, 2000)
    }

    private fun getAmountString(amount: Amount): String =
            getString(R.string.amount_value_dynamic, amount.currency, (amount.valueInCents / 100.0f))

    companion object {
        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction): Intent {
            return Intent(context, ActivePayment::class.java).also {
                it.putExtra(KEY_TRANSACTION, transaction)
            }
        }
    }
}
