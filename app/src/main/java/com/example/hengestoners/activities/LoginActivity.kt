package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.presenters.LoginPresenter
import kotlinx.android.synthetic.main.activity_hengestoners.*
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), AnkoLogger {

    lateinit var presenter: LoginPresenter

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    presenter = LoginPresenter(this)

    // Function to handle log in button
    loginButton.setOnClickListener() {

        // Gather data from the fields
        val email = loginEmail.text.toString()
        val password = loginPassword.text.toString()

        // If email or password is empty tell the user
        if (email.isEmpty() || password.isEmpty()) {
            toast("Please Enter email and Password")
        } else {
            presenter.doLogin(email, password)
        }
    }
        // Function to handle reg button
        regButton.setOnClickListener() {
            presenter.doRegister()
        }
    }
}
