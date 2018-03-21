package nl.mranderson.hackathon2018.splitpayment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_split_payment.*
import nl.mranderson.hackathon2018.R

class PaymentResultSplitFragment : Fragment() {

    private lateinit var viewModel: SplitPaymentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_split_payment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewState = SplitPaymentViewState()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val amountData = arguments?.getParcelable<SplitPaymentData>("paymentData")

        amountData?.let {
            populateAmounts(amountData)
        }
    }

    private fun populateAmounts(amountData: SplitPaymentData) {
        corporate_amt.text = amountData.corporateAmount
        personal_amt.text = amountData.personalAmount
    }

    companion object {
        fun newInstance(amount: SplitPaymentData): PaymentResultSplitFragment {
            val fragment = PaymentResultSplitFragment()
            val args = Bundle()
            args.putParcelable("paymentData", amount)
            fragment.arguments = args
            return fragment
        }
    }

}
