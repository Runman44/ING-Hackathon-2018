package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
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
