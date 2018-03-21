package nl.mranderson.hackathon2018.card

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class CardViewState {
    var data = MutableLiveData<List<Any?>>()
}

@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory(private val presenter: CardPresenter, private val viewState: CardViewState) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CardViewModel(presenter, viewState) as T
    }
}

class CardViewModel(private val presenter: CardPresenter, val viewState: CardViewState) : ViewModel() {

    init {
        presenter.start()
    }

    override fun onCleared() {
        presenter.clear()
        super.onCleared()
    }
}