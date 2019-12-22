package com.example.hengestoners.views.Login

import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.views.Base.BaseView
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class LoginView : BaseView(), AnkoLogger {

    lateinit var presenter: LoginPresenter

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)
    presenter = initPresenter(LoginPresenter(this)) as LoginPresenter

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
