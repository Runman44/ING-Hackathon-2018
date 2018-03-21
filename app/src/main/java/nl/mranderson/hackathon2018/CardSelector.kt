package nl.mranderson.hackathon2018

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentFilter.MalformedMimeTypeException
import android.nfc.NfcAdapter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast

private const val MIME_TEXT_PLAIN = "text/plain"

class CardSelector : AppCompatActivity() {

    private var nfcAdapter: NfcAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_selector)

        nfcAdapter = NfcAdapter.getDefaultAdapter(this)

        // We might be called from the system
        handleIntent(intent)
    }

    override fun onResume() {
        super.onResume()

        setupForegroundDispatch(this, nfcAdapter)

    }

    override fun onPause() {
        super.onPause()

        stopForegroundDispatch(this, nfcAdapter)
    }

    private fun handleIntent(intent : Intent) {
        when (intent.action) {
            NfcAdapter.ACTION_NDEF_DISCOVERED, NfcAdapter.ACTION_TECH_DISCOVERED, NfcAdapter.ACTION_TAG_DISCOVERED -> {
                Toast.makeText(this, "Found NFC tag", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        if (adapter == null) return

        val intent = Intent(activity.applicationContext, activity.javaClass)
        intent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP

        val pendingIntent = PendingIntent.getActivity(activity.applicationContext, 0, intent, 0)

        // Notice that this is the same filter as in our manifest.
        val intentFilter = IntentFilter()
        intentFilter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED)
        intentFilter.addCategory(Intent.CATEGORY_DEFAULT)
        try {
            intentFilter.addDataType(MIME_TEXT_PLAIN)
        } catch (e: MalformedMimeTypeException) {
            throw RuntimeException("Check your mime type.")
        }

        val filters = arrayOf(intentFilter)
        val techList = arrayOf<Array<String>>()

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList)
    }

    private fun stopForegroundDispatch(activity: Activity, adapter: NfcAdapter?) {
        if (adapter == null) return

        adapter.disableForegroundDispatch(activity)
    }

}
