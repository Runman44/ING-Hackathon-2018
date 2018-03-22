package nl.mranderson.hackathon2018.card

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_list.*
import nl.mranderson.hackathon2018.R
import nl.mranderson.hackathon2018.data.Amount
import nl.mranderson.hackathon2018.data.Card

class CardFragment : Fragment() {

    private lateinit var viewModel: CardViewModel
    private lateinit var adapter: CardSlidePagerAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    private lateinit var listener: ViewPager.OnPageChangeListener

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewState = CardViewState()
        val presenter = CardPresenter(viewState, CardInteractor())
        viewModel = ViewModelProviders.of(this, VideoViewModelFactory(presenter, viewState)).get(CardViewModel::class.java)

        adapter = CardSlidePagerAdapter(fragmentManager)
        pager.adapter = adapter
        // Disable clip to padding
        pager.clipToPadding = false
        // set padding manually, the more you set the padding the more you see of prev & next page
        pager.setPadding(110, 0, 110, 0)
        // sets a margin b/w individual pages to ensure that there is a gap b/w them
        pager.pageMargin = 2
        pager.offscreenPageLimit = 3
        listener = object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(arg0: Int) {}


            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}


            override fun onPageSelected(position: Int) {
                val card = adapter.getItem(position).card
                card_name.text = card.name
                card_days.text = createDaysString(card)
                card_amount_left.text = doubleDecimalsForRohan(card.rules.amount) + " EUR"
            }
        }
        pager.addOnPageChangeListener(listener)

        logout.setOnLongClickListener {
            FirebaseAuth.getInstance().signOut()
            true
        }

        bindViews()
    }

    fun updateCard(updatedRuleAmount: Int) {
        val card = getCard()
        card.rules.amount.valueInCents = updatedRuleAmount
        card_amount_left.text = doubleDecimalsForRohan(card.rules.amount) + " EUR"
    }

    private fun bindViews() {
        viewModel.viewState.data.observe(this, Observer { cards -> showData(cards) })
    }

    private fun showData(cards: List<Card>?) {
        if (cards != null) {
            progressBar2.visibility = View.GONE
            for (card in cards) {
                val cardImageFragment = CardImageFragment()
                cardImageFragment.card = card
                adapter.addFragment(cardImageFragment)
            }
            adapter.notifyDataSetChanged()
            listener.onPageSelected(0)
        }
    }

    private fun createDaysString(card: Card): CharSequence? {
        val builder = StringBuilder()
        if (card.rules.week.monday) {
            builder.append("MON - ")
        }
        if (card.rules.week.tuesday) {
            builder.append("TUE - ")
        }
        if (card.rules.week.wednesday) {
            builder.append("WED - ")
        }
        if (card.rules.week.thursday) {
            builder.append("THU - ")
        }
        if (card.rules.week.friday) {
            builder.append("FRI - ")
        }
        if (card.rules.week.saturday) {
            builder.append("SAT - ")
        }
        if (card.rules.week.sunday) {
            builder.append("SUN - ")
        }


        builder.append(doubleDecimalsForRohan(card.rules.initAmount))
        if (card.rules.initAmount.currency == "€") {
            builder.append(" EUR")
        } else {
            builder.append(card.rules.initAmount.currency)
        }
        return builder.toString()
    }

    private fun doubleDecimalsForRohan(amount: Amount) : String {
        return getString(R.string.amount_value_dynamic_with_no_currency, (amount.valueInCents / 100.0f))
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
