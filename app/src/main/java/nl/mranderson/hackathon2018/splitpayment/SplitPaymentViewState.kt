package nl.mranderson.hackathon2018.splitpayment

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider

class SplitPaymentViewState {
    var data = MutableLiveData<List<Any?>>()
}

@Suppress("UNCHECKED_CAST")
class VideoViewModelFactory(private val presenter: SplitPaymentPresenter, private val viewState: SplitPaymentViewState) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SplitPaymentViewModel(presenter, viewState) as T
    }
}
