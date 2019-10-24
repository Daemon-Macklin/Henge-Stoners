package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.R
import com.example.hengestoners.helpers.hillFortTotal
import com.example.hengestoners.helpers.hillFortVisited
import com.example.hengestoners.helpers.myHillFortTotal
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_hillfort_list.HomeButton
import kotlinx.android.synthetic.main.activity_hillfort_list.LogOutButton
import kotlinx.android.synthetic.main.activity_hillfort_list.SettingsButton
import kotlinx.android.synthetic.main.activity_hillfort_list.navToggleButton
import kotlinx.android.synthetic.main.activity_hillfort_list.navigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.toolbar
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SettingsActivity : AppCompatActivity() {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        app = application as MainApp

        toolbar.title = "$title Settings"
        setSupportActionBar(toolbar)
        navigationView.visibility = View.INVISIBLE

        settings_email.setText(app.signedInUser.email)
        settings_userName.setText(app.signedInUser.userName)

        totalHillForts.text = "Total HillForts ${hillFortTotal(app.users.findAll())}"
        myTotalHillForts.text = "My Total HillForts ${myHillFortTotal(app.signedInUser)}"
        totalVisitedHillForts.text = "Total HillForts Visited ${hillFortVisited(app.users.findAll())}"

        navToggleButton.setOnClickListener() {
            when(navigationView != null){
                navigationView.isVisible -> navigationView.visibility = View.INVISIBLE
                !navigationView.isVisible -> navigationView.visibility = View.VISIBLE
            }
        }

        updateUser.setOnClickListener() {
            var email = settings_email.text.toString()
            var userName = settings_userName.text.toString()

            if (email.isEmpty()) {
                email = app.signedInUser.email
            }
            if (userName.isEmpty()) {
                userName = app.signedInUser.userName
            }

            if (email == app.signedInUser.email && userName == app.signedInUser.userName) {
                toast("Email and Username Not Changed")
            } else {
                app.users.updateDetails(app.signedInUser, email, userName)
                app.signedInUser = app.users.findByEmail(email)
                toast("Updated User Daata")
                settings_email.setText(app.signedInUser.email)
                settings_userName.setText(app.signedInUser.userName)
            }
        }

        updatePassword.setOnClickListener() {
            val curPass = settings_password.text.toString()
            val newPass = settings_newPassword.text.toString()

            if (curPass.isEmpty() || newPass.isEmpty()){
                toast("Please enter Passwords")
            } else {
                val result = app.users.updatePassword(app.signedInUser, curPass, newPass)
                if(result){
                    toast("Password Updated")
                    app.signedInUser = app.users.findByEmail(app.signedInUser.email)
                } else {
                    toast("Error Updating Password")
                }
            }
        }

        HomeButton.setOnClickListener() {
            startActivity(intentFor<HillFortListActivity>())
        }

        SettingsButton.isEnabled = false

        LogOutButton.setOnClickListener() {
            app.signedInUser = UserModel()
            startActivity(intentFor<LoginActivity>())
            finish()
        }
    }
}
