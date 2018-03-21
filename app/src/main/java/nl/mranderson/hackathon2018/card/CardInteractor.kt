package nl.mranderson.hackathon2018.card

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single


class CardInteractor : CardContract.Interactor {

    override fun getData(url: String): Single<CardResponse> {
        return Single.create {

            val mAuth = FirebaseAuth.getInstance()
            var id : String?
            mAuth.signInWithEmailAndPassword("test@ing.com", "hack123")
                    .addOnCompleteListener({ task ->
                        if (task.isSuccessful) {
                            Log.d("SAYWUT", "YES")
                            // Sign in success, update UI with the signed-in user's information
                            val user = mAuth.currentUser
                            id = user?.uid

                            val db = FirebaseFirestore.getInstance()
                            val citiesRef = db.collection("cards")
                            // Create a query against the collection.
                            val query = citiesRef.whereEqualTo("authId", id)

                            query.get().addOnCompleteListener({ task2 ->
                                if (task2.isSuccessful) {
                                    for (document in task2.result) {
                                        Log.d("SAYWUT", document.id + " => " + document.data)
                                        val response = CardResponse()
                                        it.onSuccess(response)
                                    }
                                }
                            })
                        } else {
                            Log.d("SAYWUT", "FAILED")
                        }
                    })
        }
    }
}