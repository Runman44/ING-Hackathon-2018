package nl.mranderson.hackathon2018

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import nl.mranderson.hackathon2018.login.LoginActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}