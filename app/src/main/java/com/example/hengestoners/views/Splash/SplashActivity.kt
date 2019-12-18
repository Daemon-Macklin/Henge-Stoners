package com.example.hengestoners.views.Splash

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.hengestoners.R
import com.example.hengestoners.views.Login.LoginView
import org.jetbrains.anko.intentFor

// SplashActivity - For 3 second logo at start
class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long=3000

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Wait 3 seconds and then start login activity
        Handler().postDelayed({
            startActivity(intentFor<LoginView>())

            // Kill the app if the user presses back
            finish()
        }, SPLASH_TIME_OUT)
    }
}
