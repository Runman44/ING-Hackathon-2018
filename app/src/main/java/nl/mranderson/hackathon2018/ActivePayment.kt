package nl.mranderson.hackathon2018

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class ActivePayment : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_payment)
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context) = Intent(context, ActivePayment::class.java)
    }
}
