package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.hengestoners.R
import kotlinx.android.synthetic.main.activity_splash.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivity

class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT: Long=3000

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            startActivity(intentFor<LoginActivity>())

            finish()
        }, SPLASH_TIME_OUT)
    }
}
