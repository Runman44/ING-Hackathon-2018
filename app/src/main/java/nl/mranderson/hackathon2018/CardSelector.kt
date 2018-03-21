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
import nl.mranderson.hackathon2018.card.CardFragment
import nl.mranderson.hackathon2018.data.*

private const val MIME_TEXT_PLAIN = "text/plain"

class CardSelector : AppCompatActivity() {
    private val amountMap = HashMap<String, Amount>()

    private var nfcAdapter: NfcAdapter? = null

    init {
        amountMap["F7062D5B"] = Amount(4500) // White card
        amountMap["CC2583B5"] = Amount(889)
        amountMap["2C1BD7E2"] = Amount(245)
        amountMap["EC1C6AB5"] = Amount(1231)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_selector)

        val fragment: CardFragment
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

                startActivity(
                        ActivePayment.createIntent(this, Transaction(
                                Card("iban", "account", Rules("rulesid")),
                                Account("iban", 1000),
                                transitionAmount)))
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

}
