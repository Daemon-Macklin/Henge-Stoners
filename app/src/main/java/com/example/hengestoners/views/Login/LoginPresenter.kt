package com.example.hengestoners.views.Login

import com.example.hengestoners.views.HillfortList.HillFortListView
import com.example.hengestoners.views.Register.RegisterView
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class LoginPresenter(view: BaseView): BasePresenter(view) {

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
                    view!!.toast("Error Logging in")
                } else {

                    // Set the user to be the signed in user and start the list activity
                    app.signedInUser = user
                    view?.navigateTo(VIEW.LIST)
                    view!!.finish()
                }
            } else {

                // If it fails tell the user
                view!!.toast("Invalid Email or Password")
            }
        }

    fun doRegister(){
        // When pressed start the register activity
        view?.navigateTo(VIEW.REGISTER)
    }
}
