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

// SettingsActivity - Activity for user to update data and view stats
class SettingsActivity : AppCompatActivity() {

    lateinit var app : MainApp

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        app = application as MainApp

        // Set the toolbar and navbar title
        toolbar.title = "$title Settings"
        navToolBarSettings.title = app.signedInUser.userName
        setSupportActionBar(toolbar)

        // Make the navbar invisible
        navigationView.visibility = View.INVISIBLE

        // Set the email and username fields
        settings_email.setText(app.signedInUser.email)
        settings_userName.setText(app.signedInUser.userName)

        // Set the stats data(data from statshelper)
        totalHillForts.text = "Total HillForts: ${hillFortTotal(app.users.findAll())}"
        myTotalHillForts.text = "My Total HillForts: ${myHillFortTotal(app.signedInUser)}"
        totalVisitedHillForts.text = "Total HillForts Visited: ${hillFortVisited(app.users.findAll())}"
        myTotalVisitedHillForts.text = "My Total HillForts Visited: ${myHillFortVisited(app.signedInUser)}"
        totalImages.text = "Total Images:  ${imageTotal(app.users.findAll())}"
        myTotalImages.text = "My Total Images: ${myImageTotal(app.signedInUser)}"
        totalNotes.text = "Total Notes: ${notesTotal(app.users.findAll())}"
        myTotalNotes.text = "My Total Notes: ${myNotesTotal(app.signedInUser)}"
        mostHillForts.text = "User With Most Hillforts: ${userWithMostHillforts(app.users.findAll())}"

        // Function to handle update user button
        updateUser.setOnClickListener() {

            // Gather user data
            var email = settings_email.text.toString()
            var userName = settings_userName.text.toString()


            // If the fields are empty don't change them
            if (email.isEmpty()) {
                email = app.signedInUser.email
            }
            if (userName.isEmpty()) {
                userName = app.signedInUser.userName
            }

            // If the data has not changed don't update them
            if (email == app.signedInUser.email && userName == app.signedInUser.userName) {
                toast("Email and Username Not Changed")
            } else {

                // If the data has changed call the update details button
                val result = app.users.updateDetails(app.signedInUser, email, userName)
                if (result) {

                    // If the update succeeds get the updated user object with the new email
                    val user = app.users.findByEmail(email)

                    // If it not null
                    if (user != null) {

                        // Make it the signed in user and update the fields
                        app.signedInUser = user
                        toast("Updated User Data")
                        settings_email.setText(app.signedInUser.email)
                        settings_userName.setText(app.signedInUser.userName)
                    } else {

                        // Unlikely situation, but If the user is null we are in a weird state so kick the user out
                        toast("Error Updating data")
                        startActivity(intentFor<LoginActivity>())
                        finish()
                    }
                } else{
                    // If the update fails the email is already in use
                    toast("Email already in use")
                }
            }
        }


        // Function to handle update password button
        updatePassword.setOnClickListener() {

            // Gather the data
            val curPass = settings_password.text.toString()
            val newPass = settings_newPassword.text.toString()

            // If either are empty stop and tell user
            if (curPass.isEmpty() || newPass.isEmpty()){
                toast("Please enter Passwords")
            } else {

                // Else try update the password
                val result = app.users.updatePassword(app.signedInUser, curPass, newPass)
                if(result){

                    // If it succeeds get the updated user object
                    val user = app.users.findByEmail(app.signedInUser.email)
                    if(user != null){

                        // if it is not null set it as the signed in user
                        toast("Password Updated")
                        app.signedInUser = user
                    }else{

                        // Unlikely situation, but If the user is null we are in a weird state so kick the user out
                        toast("Error Updating Password")
                        startActivity(intentFor<LoginActivity>())
                        finish()
                    }
                } else {
                    toast("Error Updating Password")
                }
            }
        }

        // Method to delete user account
        deleteAccount.setOnClickListener {

            // Gather data
            val pass = settings_deletePassword.text.toString()

            // If the password is blank stop and tell user
            if(pass.isBlank()){
                toast("Please Enter Password")
            } else {

                // Else try remove the user
                val result = app.users.remove(app.signedInUser, pass)
                if(result){

                    // If it succeeds the user has been removed so kick them out
                    toast("User Removed")
                    startActivity(intentFor<LoginActivity>())
                    finish()
                } else {

                    // Else the password was wrong
                    toast("Error Updating Passwords")
                }
            }
        }

        // Function to handle when navbar toggle button is pressed
        navToggleButton.setOnClickListener() {
            when(navigationView != null){

                // Show or hide the nav bar depending on it's current state
                navigationView.isVisible -> navigationView.visibility = View.INVISIBLE
                !navigationView.isVisible -> navigationView.visibility = View.VISIBLE
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
