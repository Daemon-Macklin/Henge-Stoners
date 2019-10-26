package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import kotlinx.android.synthetic.main.activity_hengestoners.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp

        // Function to handle log in button
        loginButton.setOnClickListener() {

            // Gather data from the fields
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            // If email or password is empty tell the user
            if (email.isEmpty() || password.isEmpty()) {
                toast("Please Enter email and Password")
            } else {

                // If there is data try to log in
                val result = app.users.login(email, password)

                // If login succeeds
                if (result) {
                    // Get the users object from the list
                    val user = app.users.findByEmail(email)
                    if (user == null) {
                        toast("Error Logging in")
                    } else {

                        // Set the user to be the signed in user and start the list activity
                        app.signedInUser = user
                        startActivity(intentFor<HillFortListActivity>())
                        finish()
                    }
                } else {

                    // If it fails tell the user
                    toast("Invalid Email or Password")
                }
            }
        }

        // Function to handle reg button
        regButton.setOnClickListener() {

            // When pressed start the register activity
            startActivity(intentFor<RegisterActivity>())
        }
    }
}
