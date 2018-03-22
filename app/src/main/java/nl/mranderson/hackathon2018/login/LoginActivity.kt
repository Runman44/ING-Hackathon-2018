package nl.mranderson.hackathon2018.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import nl.mranderson.hackathon2018.CardSelector
import nl.mranderson.hackathon2018.data.Member
import nl.mranderson.hackathon2018.data.User
import java.util.*

class LoginActivity : AppCompatActivity() {
    val RC_SIGN_IN = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val providers = Arrays.asList(
                AuthUI.IdpConfig.Builder(AuthUI.EMAIL_PROVIDER).build())

        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .build(),
                RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                User.authId = user?.uid
                //TODO could do call here.
                val db = FirebaseFirestore.getInstance()
                val membersRef = db.collection("members")
                val query = membersRef.whereEqualTo("authId", User.authId)
                query.get().addOnCompleteListener({ task ->
                    if (task.isSuccessful) {

                        for(document in task.result) {
                            Member.memberId = document.id
                        }
                        val intent = Intent(this, CardSelector::class.java)
                        startActivity(intent)
                        finish()
                    }
                })
            }
        }
    }
}