package nl.mranderson.hackathon2018.card

import io.reactivex.disposables.Disposable
import nl.mranderson.hackathon2018.data.Card


class CardPresenter(private val viewState: CardViewState, private val model: CardInteractor) : CardContract.Presenter {

    private lateinit var disposable: Disposable

    fun start() {
        disposable = model.getCard("")
                .subscribe(this::getRules, this::handleException)
    }

    override fun clear() {
        disposable.dispose()
    }

    private fun getRules(cardResponse: CardWithoutRuleResponse) {
        val cards = cardResponse.cards
        if (cards != null) {
            for (cardWithoutRule in cards) {
                model.getRules(cardWithoutRule)
                        .subscribe(this::handleResponse, this::handleException)
            }
        }
    }

    private fun handleResponse(cardResponse: CardResponse) {
        val cards = cardResponse.cards
        val data = viewState.data.value

        if (data == null) {
            viewState.data.value = (cards as ArrayList<Card>)
        } else if (cards != null) {
            for (card in cards) {
                data.add(card)
            }
            viewState.data.value = data
        }
    }

    private fun handleException(throwable: Throwable) {
    }
}
