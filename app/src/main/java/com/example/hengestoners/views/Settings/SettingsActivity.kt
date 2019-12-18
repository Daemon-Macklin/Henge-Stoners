package com.example.hengestoners.views.Settings

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.views.HillfortList.HillFortListActivity
import com.example.hengestoners.views.Login.LoginActivity
import com.example.hengestoners.views.MapView.HengeStoneMapsActivity
import kotlinx.android.synthetic.main.activity_hillfort_list.HomeButton
import kotlinx.android.synthetic.main.activity_hillfort_list.LogOutButton
import kotlinx.android.synthetic.main.activity_hillfort_list.SettingsButton
import kotlinx.android.synthetic.main.activity_hillfort_list.navToggleButton
import kotlinx.android.synthetic.main.activity_hillfort_list.navigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.MapsActivityButton
import kotlinx.android.synthetic.main.activity_hillfort_list.toolbar
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

// SettingsActivity - Activity for user to update data and view stats
class SettingsActivity : AppCompatActivity() {

    lateinit var app : MainApp
    lateinit var presenter: SettingsPresenter
    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter = SettingsPresenter(this)
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

        presenter.createStats()


        // Function to handle update user button
        updateUser.setOnClickListener() {

            // Gather user data
            var email = settings_email.text.toString()
            var userName = settings_userName.text.toString()

            presenter.updateUser(email, userName)
        }


        // Function to handle update password button
        updatePassword.setOnClickListener() {

            // Gather the data
            val curPass = settings_password.text.toString()
            val newPass = settings_newPassword.text.toString()

            // If either are empty stop and tell user
            if (curPass.isEmpty() || newPass.isEmpty()) {
                toast("Please enter Passwords")
            } else {
                presenter.updatePassword(curPass, newPass)
            }
        }

        // Method to delete user account
        deleteAccount.setOnClickListener {

            // Gather data
            val pass = settings_deletePassword.text.toString()

            // If the password is blank stop and tell user
            if (pass.isBlank()) {
                toast("Please Enter Password")
            } else {
                presenter.deleteAccount(pass)
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

        MapsActivityButton.setOnClickListener {
            startActivity(intentFor<HengeStoneMapsActivity>())
        }
    }
}
