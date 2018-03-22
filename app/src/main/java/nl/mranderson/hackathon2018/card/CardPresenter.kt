package nl.mranderson.hackathon2018.card

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers


class CardPresenter(private val viewState: CardViewState, private val model: CardInteractor) : CardContract.Presenter {

    private val url = "https://www.googleapis.com/youtube/v3/search?part=snippet&fields=items(id(videoId),snippet(title,publishedAt,thumbnails(high(url))))&q=pokemon&type=video&order=date&relevanceLanguage=en-us&max-results=10&key=AIzaSyDlS5wq_ZDcOsHENER-5tsYPej6T3ziNT4&maxResults=25"
    private lateinit var disposable: Disposable

    fun start() {
        disposable = model.getData(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleException)
    }

    override fun clear() {
        disposable.dispose()
    }

    private fun handleResponse(cardResponse: CardResponse) {
        val cards = cardResponse.cards
        viewState.data.postValue(cards)
    }

    private fun handleException(throwable: Throwable) {

    }
}
