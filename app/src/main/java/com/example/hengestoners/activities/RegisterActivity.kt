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
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

// RegisterActivity - Activity to let users register
class RegisterActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        app = application as MainApp

        // Method to handel add user button
        addUserButton.setOnClickListener() {

            // Gather data
            val userName = RegisterAddUser.text.toString()
            val email = RegisterAddEmai.text.toString()
            val pass = RegisterAddPass.text.toString()

            // If all of the fields are not empty
            if(userName.isNotEmpty() && email.isNotEmpty() && pass.isNotEmpty()){

                // Create a new user and add the data
                val newUser = UserModel()
                newUser.userName = userName
                newUser.email = email
                newUser.password = pass

                // Create the new user
                val result = app.users.create(newUser)

                // If the user is created sign them in and go to the list activity
                if(result){
                    app.signedInUser = newUser
                    startActivity(intentFor<HillFortListActivity>())
                    finish()
                } else{

                    // Else the email is already in use
                    toast("Email Already in Use")
                }
            }else {

                // Else tell the user
                toast("Please Enter all fields")
            }
        }
    }
}
