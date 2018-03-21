package nl.mranderson.hackathon2018.card

import io.reactivex.Single

class CardInteractor : CardContract.Interactor {

    override fun getData(url: String): Single<CardResponse> {
        return Single.create {
            val response = CardResponse()
            it.onSuccess(response)
        }
    }
}