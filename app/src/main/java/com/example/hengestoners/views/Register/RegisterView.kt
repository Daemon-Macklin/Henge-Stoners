package com.example.hengestoners.views.Register

import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.views.Base.BaseView
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

// RegisterView - Activity to let users register
class RegisterView : BaseView(), AnkoLogger {

    lateinit var app : MainApp
    lateinit var presenter: RegisterPresenter

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = initPresenter(RegisterPresenter(this)) as RegisterPresenter
        app = application as MainApp

        // Method to handel add user button
        addUserButton.setOnClickListener() {

            // Gather data
            val userName = RegisterAddUser.text.toString()
            val email = RegisterAddEmai.text.toString()
            val pass = RegisterAddPass.text.toString()

            // If all of the fields are not empty
            if(userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()){
                presenter.doAddUser(userName, email, pass)
            }else {

                // Else tell the user
                toast("Please Enter all fields")
            }
        }
    }
}
