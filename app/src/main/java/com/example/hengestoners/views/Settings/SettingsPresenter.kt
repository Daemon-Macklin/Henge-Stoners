package com.example.hengestoners.views.Settings

import android.annotation.SuppressLint
import com.example.hengestoners.views.Login.LoginView
import com.example.hengestoners.helpers.*
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import kotlinx.android.synthetic.main.activity_settings.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast

class SettingsPresenter(view: BaseView): BasePresenter(view) {

    init {
        app = view.application as MainApp
    }


    @SuppressLint("SetTextI18n")
    fun createStats(){
        // Set the stats data(data from statshelper)
        view!!.totalHillForts.text = "Total HillForts: ${hillFortTotal(app.users.findAll())}"
        view!!.myTotalHillForts.text = "My Total HillForts: ${myHillFortTotal(app.signedInUser)}"
        view!!.totalVisitedHillForts.text = "Total HillForts Visited: ${hillFortVisited(app.users.findAll())}"
        view!!.myTotalVisitedHillForts.text = "My Total HillForts Visited: ${myHillFortVisited(app.signedInUser)}"
        view!!.totalImages.text = "Total Images:  ${imageTotal(app.users.findAll())}"
        view!!.myTotalImages.text = "My Total Images: ${myImageTotal(app.signedInUser)}"
        view!!.totalNotes.text = "Total Notes: ${notesTotal(app.users.findAll())}"
        view!!.myTotalNotes.text = "My Total Notes: ${myNotesTotal(app.signedInUser)}"
        view!!.mostHillForts.text = "User With Most Hillforts: ${userWithMostHillforts(app.users.findAll())}"
        view!!.mostHillFortsVisited.text = "User withe Most Hillforts Visited: ${userWithMostVisitedHillForts(app.users.findAll())}"
    }

    fun updateUser(emailInput: String, userNameInput: String){

        var email = emailInput
        var userName = userNameInput

        // If the fields are empty don't change them
        if (email.isEmpty() || email == app.signedInUser.email) {
            email = app.signedInUser.email
        } else if(!app.users.checkEmail(email)){
            view!!.toast("Please use Valid Email")
            return
        }
        if (userName.isEmpty()) {
            userName = app.signedInUser.userName
        }

        // If the data has not changed don't update them
        if (email == app.signedInUser.email && userName == app.signedInUser.userName) {
            view!!.toast("Email and Username Not Changed")
        } else {

            // If the data has changed call the update details button
            val result = app.users.updateDetails(app.signedInUser, email, userName)
            if (result) {

                // If the update succeeds get the updated user object with the new email
                val user = app.users.findByEmail(email)

                // If it not null
                if (user != null) {

                    // Make it the signed in user and update the fields
                    app.signedInUser = user
                    view!!.toast("Updated User Data")
                    view!!.settings_email.setText(app.signedInUser.email)
                    view!!.settings_userName.setText(app.signedInUser.userName)
                } else {

                    // Unlikely situation, but If the user is null we are in a weird state so kick the user out
                    view!!.toast("Error Updating data")
                    view?.navigateTo(VIEW.LOGIN)
                    view!!.finish()
                }
            } else {
                // If the update fails the email is already in use
                view!!.toast("Email already in use")
            }
        }
    }

    fun updatePassword(curPass: String, newPass: String){
        if(app.users.checkPass(newPass)){
            view!!.toast("Password Not Strong Enough")
        }else {
            val result = app.users.updatePassword(app.signedInUser, curPass, newPass)
            if (result) {
                // If it succeeds get the updated user object
                val user = app.users.findByEmail(app.signedInUser.email)
                if (user != null) {

                    // if it is not null set it as the signed in user
                    view!!.toast("Password Updated")
                    app.signedInUser = user
                } else {
                    // Unlikely situation, but If the user is null we are in a weird state so kick the user out
                    view!!.toast("Error Updating Password")
                    view?.navigateTo(VIEW.LOGIN)
                    view!!.finish()
                }
            } else {
                view!!.toast("Error Updating Password")
            }
        }
    }

    fun deleteAccount(pass: String){
        // If the password is blank stop and tell user
        // Else try remove the user
        val result = app.users.remove(app.signedInUser, pass)
        if(result){

            // If it succeeds the user has been removed so kick them out
            view!!.toast("User Removed")
            view?.navigateTo(VIEW.LOGIN)
            view!!.finish()

        } else {

            // Else the password was wrong
            view!!.toast("Error Updating Passwords")
        }
    }
}