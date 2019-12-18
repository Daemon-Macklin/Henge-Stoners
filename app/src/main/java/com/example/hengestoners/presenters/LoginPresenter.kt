package com.example.hengestoners.presenters

import com.example.hengestoners.activities.HillFortListActivity
import com.example.hengestoners.activities.LoginActivity
import com.example.hengestoners.activities.RegisterActivity
import com.example.hengestoners.main.MainApp
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginPresenter(val view: LoginActivity) {

    lateinit var app : MainApp

    init{
        app = view.application as MainApp
    }

    fun doLogin(email: String, password: String){
            // If there is data try to log in
            val result = app.users.login(email, password)

            // If login succeeds
            if (result) {
                // Get the users object from the list
                val user = app.users.findByEmail(email)
                if (user == null) {
                    view.toast("Error Logging in")
                } else {

                    // Set the user to be the signed in user and start the list activity
                    app.signedInUser = user
                    view.startActivity(view.intentFor<HillFortListActivity>())
                    view.finish()
                }
            } else {

                // If it fails tell the user
                view.toast("Invalid Email or Password")
            }
        }

    fun doRegister(){
        // When pressed start the register activity
        view.startActivity(view.intentFor<RegisterActivity>())
    }
    }
