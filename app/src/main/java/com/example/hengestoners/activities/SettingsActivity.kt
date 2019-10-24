package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.R
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        toolbar.title = "$title Settings"
        setSupportActionBar(toolbar)
        navigationView.visibility = View.INVISIBLE

        navToggleButton.setOnClickListener() {
            when(navigationView != null){
                navigationView.isVisible -> navigationView.visibility = View.INVISIBLE
                !navigationView.isVisible -> navigationView.visibility = View.VISIBLE
            }
        }

        HomeButton.setOnClickListener() {
            startActivity(intentFor<HillFortListActivity>())
        }

        SettingsButton.isEnabled = false

        LogOutButton.setOnClickListener() {
            toast("Logout")
        }
    }
}
