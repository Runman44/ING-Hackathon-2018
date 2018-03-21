package nl.mranderson.hackathon2018.splitpayment

import android.arch.lifecycle.ViewModel

class SplitPaymentViewModel(val presenter: SplitPaymentPresenter, val viewState: SplitPaymentViewState) : ViewModel() {

    init {
        presenter.start()
    }

    override fun onCleared() {
        presenter.clear()
        super.onCleared()
    }

}
