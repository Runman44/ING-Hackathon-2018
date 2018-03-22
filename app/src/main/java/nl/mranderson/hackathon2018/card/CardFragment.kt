package nl.mranderson.hackathon2018.card

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
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
                val cardImageFragment = CardImageFragment()
                cardImageFragment.card = card
                adapter.addFragment(cardImageFragment)
                card_name.text = card.name
                card_days.text = createDaysString(card)

            }
            adapter.notifyDataSetChanged()
        }
    }

    private fun createDaysString(card: Card): CharSequence? {
        val builder = StringBuilder()
        when {
            card.rules.week.monday -> builder.append("MON - ")
            card.rules.week.tuesday -> builder.append("TUE - ")
            card.rules.week.wednesday -> builder.append("WED - ")
            card.rules.week.thursday -> builder.append("THU - ")
            card.rules.week.friday -> builder.append("FRI - ")
            card.rules.week.saturday -> builder.append("SAT - ")
            card.rules.week.sunday -> builder.append("SUN")
        }
        return builder.toString()
    }

    fun getCard(): Card {
        return adapter.getItem(pager.currentItem).card
    }

    class CardSlidePagerAdapter(supportFragmentManager: FragmentManager?) : FragmentPagerAdapter(supportFragmentManager) {
        private var list: ArrayList<CardImageFragment> = ArrayList()

        override fun getItem(position: Int): CardImageFragment {
            return list[position]
        }

        override fun getCount(): Int {
            return list.size
        }

        fun addFragment(fragment: CardImageFragment) {
            list.add(fragment)
        }
    }
}
