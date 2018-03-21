package nl.mranderson.hackathon2018.card

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Single



class CardInteractor : CardContract.Interactor {

    override fun getData(url: String): Single<CardResponse> {
        return Single.create {

            val database = FirebaseDatabase.getInstance()
            val ref = database.getReference("cards")

            val mAuth = FirebaseAuth.getInstance()
            val currentUser = mAuth.currentUser
            val id = currentUser?.uid


            ref.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    val value = dataSnapshot.getValue(String::class.java)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Failed to read value
                }
            })

            val response = CardResponse()
            it.onSuccess(response)
        }
    }
}