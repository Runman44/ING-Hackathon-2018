package nl.mranderson.hackathon2018.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import nl.mranderson.hackathon2018.CardSelector
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
                //TODO save user in object class.
                val user = FirebaseAuth.getInstance().currentUser
                Log.v("YOLO", "dsds" + user?.uid)
                val intent = Intent(this, CardSelector::class.java)
                startActivity(intent)
                finish()
            } else {
                // Sign in failed, check response for error code
                // ...
            }
        }
    }
}