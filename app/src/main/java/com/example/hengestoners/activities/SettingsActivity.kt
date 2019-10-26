package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.R
import com.example.hengestoners.helpers.*
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
        navToolBarSettings.title = app.signedInUser.userName
        setSupportActionBar(toolbar)
        navigationView.visibility = View.INVISIBLE

        settings_email.setText(app.signedInUser.email)
        settings_userName.setText(app.signedInUser.userName)

        totalHillForts.text = "Total HillForts: ${hillFortTotal(app.users.findAll())}"
        myTotalHillForts.text = "My Total HillForts: ${myHillFortTotal(app.signedInUser)}"
        totalVisitedHillForts.text = "Total HillForts Visited: ${hillFortVisited(app.users.findAll())}"
        myTotalVisitedHillForts.text = "My Total HillForts Visited: ${myHillFortVisited(app.signedInUser)}"
        totalImages.text = "Total Images:  ${imageTotal(app.users.findAll())}"
        myTotalImages.text = "My Total Images: ${myImageTotal(app.signedInUser)}"
        totalNotes.text = "Total Notes: ${notesTotal(app.users.findAll())}"
        myTotalNotes.text = "My Total Notes: ${myNotesTotal(app.signedInUser)}"
        mostHillForts.text = "User With Most Hillforts: ${userWithMostHillforts(app.users.findAll())}"

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
                val user = app.users.findByEmail(email)
                if (user != null) {
                    app.signedInUser = user
                    toast("Updated User Data")
                    settings_email.setText(app.signedInUser.email)
                    settings_userName.setText(app.signedInUser.userName)
                } else{
                    toast("Error Updating data")
                    startActivity(intentFor<LoginActivity>())
                    finish()
                }
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
                    val user = app.users.findByEmail(app.signedInUser.email)
                    if(user != null){
                        toast("Password Updated")
                        app.signedInUser = user
                    }else{
                        toast("Error Updating Password")
                        startActivity(intentFor<LoginActivity>())
                        finish()
                    }
                } else {
                    toast("Error Updating Password")
                }
            }
        }

        deleteAccount.setOnClickListener {
            val pass = settings_deletePassword.text.toString()
            if(pass.isBlank()){
                toast("Please Enter Password")
            } else {
                val result = app.users.remove(app.signedInUser, pass)
                if(result){
                    toast("User Removed")
                    startActivity(intentFor<LoginActivity>())
                    finish()
                } else {
                    toast("Error Updating Passwords")
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
