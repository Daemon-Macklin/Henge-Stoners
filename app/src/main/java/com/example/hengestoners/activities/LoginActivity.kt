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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        app = application as MainApp


        loginButton.setOnClickListener() {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            val result = app.users.login(email, password)

            if(result){
                app.signedInUser = app.users.findByEmail(email)
                startActivity(intentFor<HillFortListActivity>())
            }else{
                toast("Invalid Email or Password")
            }
        }

        regButton.setOnClickListener() {
            val newEmail = loginEmail.text.toString()
            val newPassword = loginPassword.text.toString()

            if(newEmail.isNotEmpty() && newPassword.isNotEmpty()){

                val newUser = UserModel()
                newUser.email = newEmail
                newUser.password = newPassword
                app.users.create(newUser)

            } else {
                toast("Please Enter Username and Password")
            }
        }
    }
}
