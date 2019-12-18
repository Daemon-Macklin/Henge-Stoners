package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.helpers.generateSalt
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.presenters.RegisterPresenter
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

// RegisterActivity - Activity to let users register
class RegisterActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp
    lateinit var presenter: RegisterPresenter

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        presenter = RegisterPresenter(this)
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
