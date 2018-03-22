package nl.mranderson.hackathon2018

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.transition.Transition
import android.view.View
import android.view.animation.AnimationSet
import kotlinx.android.synthetic.main.activity_split_payment_screen.*
import nl.mranderson.hackathon2018.card.CardImageFragment
import nl.mranderson.hackathon2018.data.Transaction
import nl.mranderson.hackathon2018.splitpayment.SplitPaymentData

class PaymentResult : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split_payment_screen)

        window.sharedElementEnterTransition.addListener(object : Transition.TransitionListener {
            override fun onTransitionEnd(transition: Transition?) {
                val animationSet = AnimatorSet()

                val endHeight = corporate_container.y
                val endElevation = corporate_container.elevation
                val corp_dest_x = corporate_container.x
                val personal_dest_x = personal_container.x

                corporate_container.elevation = -10f
                personal_container.elevation = -10f

                corporate_container.y = total_amt.y
                personal_container.y = total_amt.y
                corporate_container.x = (total_amt.x + total_amt.width/2) - corporate_container.width/2
                personal_container.x = (total_amt.x + total_amt.width/2) - personal_container.width/2
                corporate_container.visibility = View.VISIBLE
                personal_container.visibility = View.VISIBLE
                personal_amt_value.alpha = 0f
                corporate_amt_value.alpha = 0f

                val dropDownCorp = ObjectAnimator.ofFloat(corporate_container, "y", endHeight)
                val dropDownPersonal = ObjectAnimator.ofFloat(personal_container, "y", endHeight)
                val elevateCorp = ObjectAnimator.ofFloat(corporate_container, "elevation", endElevation)
                val elevatePersonal = ObjectAnimator.ofFloat(personal_container, "elevation", endElevation)

                val fadeCorporate = ObjectAnimator.ofFloat(corporate_container, "alpha", 0f, 1f)
                val fadePersonal = ObjectAnimator.ofFloat(personal_container, "alpha", 0f, 1f)

                animationSet.play(dropDownCorp).with(dropDownPersonal).with(elevateCorp).with(elevatePersonal).with(fadePersonal).with(fadeCorporate)
                animationSet.setDuration(500)
                animationSet.start()
                animationSet.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {
                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        val animationSet = AnimatorSet()
                        val moveCorp = ObjectAnimator.ofFloat(corporate_container, "x", corp_dest_x)
                        val movePersonal = ObjectAnimator.ofFloat(personal_container, "x", personal_dest_x)

                        val fadeCorporate = ObjectAnimator.ofFloat(corporate_amt_value, "alpha", 1f)
                        val fadePersonal = ObjectAnimator.ofFloat(personal_amt_value, "alpha", 1f)

                        animationSet.play(moveCorp).with(movePersonal).with(fadeCorporate).with(fadePersonal)
                        animationSet.setDuration(500)
                        animationSet.start()
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

//
//                corporate_amt.translationX = (total_amt.x + total_amt.width/2) - corporate_amt.x
//                corporate_amt.translationY = (total_amt.y + total_amt.height/2) - corporate_amt.y
//                corporate_amt.animate()
//                        .alpha(1f)
//                        .translationX(0f)
//                        .translationY(0f)
//                        .setDuration(2000)

            }

            override fun onTransitionResume(transition: Transition?) {
            }

            override fun onTransitionPause(transition: Transition?) {
            }

            override fun onTransitionCancel(transition: Transition?) {
            }

            override fun onTransitionStart(transition: Transition?) {
            }

        })

        initViews()
    }

    private fun initViews() {
        val cardFragment = CardImageFragment()
        supportFragmentManager.beginTransaction()
                .add(R.id.cardFragment, cardFragment)
                .commit()

        val transaction = intent.extras.get(KEY_TRANSACTION) as Transaction

        val payment = SplitPaymentData("5.00", "2.00")

        populateAmounts(transaction, payment)
    }

    private fun populateAmounts(transaction: Transaction, amountData: SplitPaymentData?) {
        amountData?.let {
            total_amt.text = "${transaction.amount.currency} ${(transaction.amount.valueInCents / 100.0f)}"
            corporate_amt_value.text = getString(R.string.amount_value, it.corporateAmount)
            personal_amt_value.text = getString(R.string.amount_value, it.personalAmount)
        }
    }

    companion object {
        @JvmStatic
        fun createIntent(context: Context, transaction: Transaction) = Intent(context, PaymentResult::class.java).also {
            it.putExtra(KEY_TRANSACTION, transaction)
        }
    }
}
