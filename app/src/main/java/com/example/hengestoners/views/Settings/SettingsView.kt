package com.example.hengestoners.views.Settings

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.views.HillfortList.HillFortListView
import com.example.hengestoners.views.Login.LoginView
import com.example.hengestoners.views.MapView.MapViewView
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Navigation.NavigationPresenter
import kotlinx.android.synthetic.main.activity_hillfort_list.toolbar
import kotlinx.android.synthetic.main.activity_map_view.*
import kotlinx.android.synthetic.main.activity_settings.*
import kotlinx.android.synthetic.main.content_nav_bar.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

// SettingsView - Activity for user to update data and view stats
class SettingsView : BaseView() {

    lateinit var app : MainApp
    lateinit var presenter: SettingsPresenter
    lateinit var nagivation: NavigationPresenter
    var x1 = 0.toFloat()
    var x2 = 0.toFloat()
    val MIN_DISTANCE = 150

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        presenter = initPresenter(SettingsPresenter(this)) as SettingsPresenter
        nagivation = initPresenter(NavigationPresenter(this)) as NavigationPresenter

        setContentView(R.layout.activity_settings)

        app = application as MainApp

        // Set the toolbar and navbar title
        super.init(toolbar, true)


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


        HomeButton.setOnClickListener() {
            nagivation.toHome()
        }

        SettingsButton.isEnabled = false

        LogOutButton.setOnClickListener() {
            nagivation.toLogOut()
        }

        MapsActivityButton.setOnClickListener {
            nagivation.toMapView()
        }
        navToolBar.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> x1 = event.getX()
                MotionEvent.ACTION_UP -> {
                    x2 = event.getX()
                    val deltaX = x2 - x1
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if(x2 > x1){
                            // Left to Right
                            nagivation.toHome()
                        }else{
                            // Right to Left
                            nagivation.toMapView()
                        }
                    }
                }
            }
            true
        }

    }
}
