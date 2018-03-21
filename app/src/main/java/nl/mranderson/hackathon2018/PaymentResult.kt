package nl.mranderson.hackathon2018

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.mranderson.hackathon2018.splitpayment.PaymentResultSplitFragment
import nl.mranderson.hackathon2018.splitpayment.SplitPaymentData

class PaymentResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_payment_screen)
        initFragment()
    }

    private fun initFragment() {
        val payment = SplitPaymentData("5", "2")

        val fragment = PaymentResultSplitFragment.newInstance(payment)
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
    }
}
