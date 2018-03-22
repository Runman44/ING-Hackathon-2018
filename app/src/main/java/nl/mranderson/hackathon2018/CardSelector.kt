package nl.mranderson.hackathon2018

import android.app.Activity
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Explode
import android.view.Window
import nl.mranderson.hackathon2018.card.CardFragment
import nl.mranderson.hackathon2018.data.Account
import nl.mranderson.hackathon2018.data.Amount
import nl.mranderson.hackathon2018.data.Transaction
import nl.mranderson.hackathon2018.data.toHex

private const val MIME_TEXT_PLAIN = "text/plain"

const val TRANSACTION_REQUEST_CODE = 9
const val UPDATED_RULE_AMOUNT = "updatedRuleAmount"

class CardSelector : AppCompatActivity() {
    private val amountMap = HashMap<String, Amount>()

    private var nfcAdapter: NfcAdapter? = null

    init {
        amountMap["F7062D5B"] = Amount(4500) // White card
        amountMap["CC2583B5"] = Amount(889)
        amountMap["2C1BD7E2"] = Amount(245)
        amountMap["EC1C6AB5"] = Amount(1231)
    }

    private lateinit var fragment: CardFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        with(window) {
            requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)

            // set an exit transition
            exitTransition = Explode()
        }

        setContentView(R.layout.activity_card_selector)

        if (savedInstanceState == null) {
            fragment = CardFragment()
            supportFragmentManager
                    .beginTransaction()
                    .add(R.id.cardFragment, fragment, "CARDTAG")
                    .commit()
        }

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // We might be called from the system
        handleIntent(intent)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, CardSelector::class.java)
    }

    override fun onNewIntent(intent: Intent?) {
        intent?.let {
            handleIntent(it)
        }
    }

    override fun onResume() {
        super.onResume()

        setupForegroundDispatch(this, nfcAdapter)
    }

    override fun onPause() {
        super.onPause()

        stopForegroundDispatch(this, nfcAdapter)
    }

    private fun handleIntent(intent: Intent) {
        when (intent.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED, NfcAdapter.ACTION_TAG_DISCOVERED -> {
                val tagId = intent.extras.getByteArray(NfcAdapter.EXTRA_ID)
                val tagTag: Tag = intent.extras.getParcelable(NfcAdapter.EXTRA_TAG)

                val transitionAmount = amountMap[tagId.toHex()] ?: Amount(0)

                startActivityForResult(
                        ActivePayment.createIntent(this, Transaction(
                                fragment.getCard(),
                                Account("iban", 1000),
                                transitionAmount)), TRANSACTION_REQUEST_CODE)
            }
        }
    }

    private fun setupForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        if (adapter == null) return

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        val techIntentFilter = IntentFilter()
        techIntentFilter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED)

        adapter.enableForegroundDispatch(activity, pendingIntent, null, null)
    }

    private fun stopForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        if (adapter == null) return

        adapter.disableForegroundDispatch(activity)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == TRANSACTION_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            updateRuleValue(data)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateRuleValue(intent: Intent) {
        val updatedRuleAmount = intent.getIntExtra(UPDATED_RULE_AMOUNT, 0)
        fragment.updateCard(updatedRuleAmount)
    }


}
