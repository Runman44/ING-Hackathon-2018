package nl.mranderson.hackathon2018.splitpayment

class SplitPaymentPresenter(private val viewState: SplitPaymentViewState, private val amount: SplitPaymentData?) : SplitPaymentPresenterContract {

    override fun start() {
        if (amount != null) {
            viewState.data.postValue(amount)
        } else {
            viewState.isFailed.postValue(true)
        }
    }

    override fun clear() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
