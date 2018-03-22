package nl.mranderson.hackathon2018.card

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_list.*
import nl.mranderson.hackathon2018.R
import nl.mranderson.hackathon2018.data.Card

class CardFragment : Fragment() {

    private lateinit var viewModel: CardViewModel
    private lateinit var adapter: CardSlidePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewState = CardViewState()
        val presenter = CardPresenter(viewState, CardInteractor())
        viewModel = ViewModelProviders.of(this, VideoViewModelFactory(presenter, viewState)).get(CardViewModel::class.java)

        adapter = CardSlidePagerAdapter(fragmentManager)
        pager.adapter = adapter
        bindViews()
    }

    private fun bindViews() {
        viewModel.viewState.data.observe(this, Observer { cards -> showData(cards) })
    }

    private fun showData(cards: List<Card>?) {
        if (cards != null) {
            for (card in cards) {
                adapter.addFragment(CardImageFragment())

            }
            adapter.notifyDataSetChanged()
        }
    }

    class CardSlidePagerAdapter(supportFragmentManager: FragmentManager?) : FragmentStatePagerAdapter(supportFragmentManager) {
        private var list: ArrayList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }

        fun addFragment(fragment: Fragment) {
            list.add(fragment)
        }
    }
}
