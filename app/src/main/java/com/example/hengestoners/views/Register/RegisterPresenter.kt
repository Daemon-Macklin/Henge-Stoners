package com.example.hengestoners.views.Register

import com.example.hengestoners.views.HillfortList.HillFortListActivity
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.UserModel
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class RegisterPresenter(val view: RegisterActivity) {

    lateinit var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun doAddUser(userName: String, email:String, pass: String){

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
                view.startActivity(view.intentFor<HillFortListActivity>())
                view.finish()
            } else{

                // Else the email is already in use
                view.toast("Email Already in Use")
            }
    }
}