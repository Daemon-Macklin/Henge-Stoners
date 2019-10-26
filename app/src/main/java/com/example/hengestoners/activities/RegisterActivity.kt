package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.helpers.generateSalt
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class RegisterActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        app = application as MainApp

        addUserButton.setOnClickListener() {
            val userName = RegisterAddUser.text.toString()
            val email = RegisterAddEmai.text.toString()
            val pass = RegisterAddPass.text.toString()

            if(userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()){
                val newUser = UserModel()
                newUser.userName = userName
                newUser.email = email
                newUser.password = pass
                val result = app.users.create(newUser)
                if(result){
                    app.signedInUser = newUser
                    finish()
                } else{
                    toast("Email Already in Use")
                }
            }else {
                toast("Please Enter all fields")
            }
        }
    }
}
