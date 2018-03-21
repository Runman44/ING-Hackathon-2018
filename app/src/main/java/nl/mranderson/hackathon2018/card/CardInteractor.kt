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

                                val week = Week(task.result.documents[0].get("monday") as Boolean,
                                        task.result.documents[0].get("tuesday") as Boolean,
                                        task.result.documents[0].get("wednesday") as Boolean,
                                        task.result.documents[0].get("thursday") as Boolean,
                                        task.result.documents[0].get("friday") as Boolean,
                                        task.result.documents[0].get("saturday") as Boolean,
                                        task.result.documents[0].get("sunday") as Boolean)

                                val rule = Rules(task.result.documents[0].get("name") as String,
                                        task.result.documents[0].get("accumulate") as Boolean,
                                        Amount(task.result.documents[0].get("amount") as Int),
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