package nl.mranderson.hackathon2018.card

import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single
import nl.mranderson.hackathon2018.data.*


class CardInteractor : CardContract.Interactor {

    override fun getData(url: String): Single<CardResponse> {
        return Single.create {
            val db = FirebaseFirestore.getInstance()
            val cardsRef = db.collection("cards")
            val query = cardsRef.whereEqualTo("authId", User.authId)

            query.get().addOnCompleteListener({ task ->
                if (task.isSuccessful) {
                    val cards = ArrayList<Card>()
                    for (document in task.result) {
                        val get = document.get("rules")
                        val documentId = (get as ArrayList<*>)[0]
                        val rulesRef = db.document("rules/" + documentId)
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

                                cards.add(Card("accountId", "iban", rule))
                            }
                        })
                    }

                    val response = CardResponse()
                    response.cards = cards
                    it.onSuccess(response)

                }
            })
        }
    }
}