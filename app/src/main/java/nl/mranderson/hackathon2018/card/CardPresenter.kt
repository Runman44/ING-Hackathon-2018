package nl.mranderson.hackathon2018.card

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class CardPresenter(private val viewState: CardViewState, private val model: CardInteractor) : CardContract.Presenter {

     private lateinit var disposable: Disposable

    fun start() {
        disposable = model.getCard("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::getRules, this::handleException)
    }

    override fun clear() {
        disposable.dispose()
    }

    private fun getRules(cardResponse: CardWithoutRuleResponse) {
        val cards = cardResponse.cards
        if (cards != null) {
            for (cardWithoutRule in cards) {
                disposable = model.getRules(cardWithoutRule)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(this::handleResponse, this::handleException)
            }
        }
    }

    private fun handleResponse(cardResponse: CardResponse) {
        val cards = cardResponse.cards
        viewState.data.postValue(cards)
    }

    private fun handleException(throwable: Throwable) {

    }
}
