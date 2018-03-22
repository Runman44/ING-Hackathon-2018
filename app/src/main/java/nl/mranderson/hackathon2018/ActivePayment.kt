package nl.mranderson.hackathon2018

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.ActivityOptionsCompat
import android.support.v7.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.android.synthetic.main.activity_active_payment.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Amount
import nl.mranderson.hackathon2018.data.Transaction
import nl.mranderson.hackathon2018.data.User

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
        doACall(transaction)
    }

    private fun doACall(transaction: Transaction) {
        val db = FirebaseFirestore.getInstance()
        val membersRef = db.collection("members")
        val query = membersRef.whereEqualTo("authId", User.authId)
        query.get().addOnCompleteListener({ task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val blah = document.get("accountId") as String
                    db.document("accounts/" + blah).get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val balance = task2.result.get("balance") as Long
                            val corporateAmount = transaction.card.rules.amount
                            val updateRuleValue = getUpdateRuleValue(transaction.amount.valueInCents, corporateAmount.valueInCents)
                            val updatedBalance = balance - updateRuleValue
                            val data = HashMap<String, Any>()
                            data.put("balance", updatedBalance)
                            db.document("accounts/" + blah).set(data, SetOptions.merge())

                            showResults(transaction)
                        }
                    }
                }
            }
        })
    }

    private fun getUpdateRuleValue(transactionAmount: Int, ruleAmount: Int): Int {
        val difference = ruleAmount - transactionAmount
        return if (difference < 0) ruleAmount else difference
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
