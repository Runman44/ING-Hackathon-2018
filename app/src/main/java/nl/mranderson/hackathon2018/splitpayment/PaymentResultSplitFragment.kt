package nl.mranderson.hackathon2018.splitpayment

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_split_payment.*
import nl.mranderson.hackathon2018.R
import java.math.BigDecimal

class PaymentResultSplitFragment : Fragment() {

    private lateinit var viewModel: SplitPaymentViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_split_payment, container, false)
    }

    private fun bindViews() {
        viewModel.viewState.data.observe(this, Observer { data -> populateAmounts(data) })
        viewModel.viewState.isFailed.observe(this, Observer { isFailed -> showFailedState(isFailed) })
    }

    private fun showFailedState(failed: Boolean?) {
        // TODO show failed view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val amountData = arguments?.getParcelable<SplitPaymentData>("paymentData")
        val viewState = SplitPaymentViewState()
        val presenter = SplitPaymentPresenter(viewState, amountData)
        viewModel = ViewModelProviders.of(this, VideoViewModelFactory(presenter, viewState)).get(SplitPaymentViewModel::class.java)

        bindViews()

        viewModel.presenter.start()
    }

    private fun populateAmounts(amountData: SplitPaymentData?) {
        amountData?.let {
            val totalAmount = BigDecimal(it.corporateAmount) + BigDecimal(it.personalAmount)
            total_amt.text = getString(R.string.amount_value, totalAmount.toString())
            corporate_amt.text = getString(R.string.amount_value, it.corporateAmount)
            personal_amt.text = getString(R.string.amount_value, it.personalAmount)
        }
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
