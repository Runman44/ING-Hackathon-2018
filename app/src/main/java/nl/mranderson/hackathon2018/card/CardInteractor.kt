package nl.mranderson.hackathon2018.card

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import io.reactivex.Single


class CardInteractor : CardContract.Interactor {

    override fun getData(url: String): Single<CardResponse> {
        return Single.create {

            val mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            val id = currentUser?.uid

            val db = FirebaseFirestore.getInstance()


            db.collection("cards")
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            for (document in task.result) {
                                Log.d("TAG", document.id + " => " + document.data)
                            }
                        } else {
//                            Log.w(TAG, "Error getting documents.", task.exception)
                        }
                    }

            val response = CardResponse()
            it.onSuccess(response)
        }
    }
}