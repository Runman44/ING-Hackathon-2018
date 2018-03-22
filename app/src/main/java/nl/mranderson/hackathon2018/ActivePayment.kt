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
import java.util.*

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
        if (requestCode == PAYMENT_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            val updatedAmount = data.extras.getInt(UPDATED_RULE_AMOUNT)
            val resultIntent = Intent()
            resultIntent.putExtra(UPDATED_RULE_AMOUNT, updatedAmount)
            setResult(Activity.RESULT_OK, resultIntent)
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
        db.collection("accounts").document(transaction.card.rules.accountId)
                .get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val balance = task.result.get("balance") as Long

                val corporateAmount = transaction.card.rules.amount
                val amountToSubtract = getUpdateRuleValue(transaction.amount.valueInCents, corporateAmount.valueInCents)
                val updatedBalance = balance - amountToSubtract
                val data = HashMap<String, Any>()
                data.put("balance", updatedBalance)
                db.document("accounts/" + transaction.card.rules.accountId).set(data, SetOptions.merge())
                updateTransactionForCorporate(amountToSubtract, transaction)
                if (transaction.amount.valueInCents < corporateAmount.valueInCents) {
                    showResults(transaction)
                } else {
                    subtractFromPrivate(transaction)
                }
            }
        }
    }

    private fun updateTransactionForCorporate(amountToSubtract: Int, transaction: Transaction) {
        val db = FirebaseFirestore.getInstance()
        db.collection("members").document(transaction.card.memberId).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val memberName = task.result.get("name") as String
                val db2 = FirebaseFirestore.getInstance()
                val transactionsRef = db2.collection("transactions")
                val data = HashMap<String, Any>()
                data.put("amount", amountToSubtract)
                data.put("date", Date())
                data.put("description", "Lunch Paid by " + memberName)
                data.put("fromAccount", transaction.card.rules.accountId)
                data.put("toAccount", "ggtqRbS4UtYq9CO0l50Q")
                transactionsRef.add(data)
            }
        }

    }

    private fun subtractFromPrivate(transaction: Transaction) {
        // Stage 2 if the amount is too high
        val db = FirebaseFirestore.getInstance()
        val membersRef = db.collection("members")
        val query = membersRef.whereEqualTo("authId", User.authId)
        query.get().addOnCompleteListener({ task ->
            if (task.isSuccessful) {
                for (document in task.result) {
                    val accountId = document.get("accountId") as String
                    db.document("accounts/" + accountId).get().addOnCompleteListener { task2 ->
                        if (task2.isSuccessful) {
                            val balance = task2.result.get("balance") as Long
                            val corporateAmount = transaction.card.rules.amount
                            val amountToSubtract = getUpdatePrivateValue(transaction.amount.valueInCents, corporateAmount.valueInCents)
                            val updatedBalance = balance - amountToSubtract
                            val data = HashMap<String, Any>()
                            data.put("balance", updatedBalance)
                            db.document("accounts/" + accountId).set(data, SetOptions.merge())

                            updateTransactionForPrivate(amountToSubtract, accountId)
                            showResults(transaction)
                        }
                    }
                }
            }
        })
    }

    private fun updateTransactionForPrivate(amountToSubtract: Int, accountId: String) {
        val db = FirebaseFirestore.getInstance()
        val transactionsRef = db.collection("transactions")
        val data = HashMap<String, Any>()
        data.put("amount", amountToSubtract)
        data.put("date", Date())
        data.put("description", "Subway")
        data.put("fromAccount", accountId)
        data.put("toAccount", "ggtqRbS4UtYq9CO0l50Q")
        transactionsRef.add(data)
    }

    private fun getUpdateRuleValue(transactionAmount: Int, ruleAmount: Int): Int {
        val difference = transactionAmount - ruleAmount
        return when {
            difference == transactionAmount -> 0
            difference > 0 -> ruleAmount
            else -> transactionAmount
        }
    }

    private fun getUpdatePrivateValue(transactionAmount: Int, ruleAmount: Int) =
            transactionAmount - ruleAmount

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
