package nl.mranderson.hackathon2018.card

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import nl.mranderson.hackathon2018.data.*


class CardInteractor : CardContract.Interactor {

    override fun getRules(response: CardWithoutRule): Single<CardResponse> {
        return Single.create {
            val cards = ArrayList<Card>()

            val db = FirebaseFirestore.getInstance()
            val rulesRef = db.document("rules/" + response.rule)

            rulesRef.get().addOnCompleteListener({ task2 ->
                if (task2.isSuccessful) {

                    val week = Week(task2.result.get("monday") as Boolean,
                            task2.result.get("tuesday") as Boolean,
                            task2.result.get("wednesday") as Boolean,
                            task2.result.get("thursday") as Boolean,
                            task2.result.get("friday") as Boolean,
                            task2.result.get("saturday") as Boolean,
                            task2.result.get("sunday") as Boolean)

                    val rule = Rules(task2.result.get("name") as String,
                            task2.result.get("accumulate") as Boolean,
                            Amount((task2.result.get("amount") as String).toInt()),
                            week)

                    cards.add(Card(response.name, response.memberId, rule))

                    val response = CardResponse()
                    response.cards = cards
                    it.onSuccess(response)
                }
            })
        }
    }

    override fun getCard(url: String): Single<CardWithoutRuleResponse> {
        return Single.create {
            val db = FirebaseFirestore.getInstance()
            val cardsRef = db.collection("cards")
            val query = cardsRef.whereEqualTo("memberId", Member.memberId)

            query.get().addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    val cards2 = ArrayList<CardWithoutRule>()

                    for (document in task.result) {
                        val name = document.data.get("name") as String
                        val memberId = document.data.get("memberId") as String
                        val ruleId = document.data.get("ruleId") as String
                        cards2.add(CardWithoutRule(name, memberId, ruleId))
                    }

                    val response2 = CardWithoutRuleResponse()
                    response2.cards = cards2
                    it.onSuccess(response2)
                }
            })
        }
    }
}