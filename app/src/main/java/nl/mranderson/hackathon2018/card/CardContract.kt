package nl.mranderson.hackathon2018.card

import io.reactivex.Single

interface CardContract {

    interface Presenter {

        fun clear()
    }

    interface Interactor {
        fun getData(url: String): Single<*>
    }
}
