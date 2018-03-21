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
import android.widget.Toast
import nl.mranderson.hackathon2018.card.CardFragment
import nl.mranderson.hackathon2018.data.*

private const val MIME_TEXT_PLAIN = "text/plain"

class CardSelector : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

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
                startActivity(
                        ActivePayment.createIntent(this, Transaction(
                        Card("iban", "account", Rules("rulesid")),
                        Account("iban", 1000),
                        Amount(30))))
                Toast.makeText(this, "Found NFC tag ${tagId.contentToString()} ${tagTag}", Toast.LENGTH_SHORT).show()
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
