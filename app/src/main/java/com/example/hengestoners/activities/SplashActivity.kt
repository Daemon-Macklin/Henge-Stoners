package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.hengestoners.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

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
            startActivity(intentFor<LoginActivity>())

            // Kill the app if the user presses back
            finish()
        }, SPLASH_TIME_OUT)
    }
}
