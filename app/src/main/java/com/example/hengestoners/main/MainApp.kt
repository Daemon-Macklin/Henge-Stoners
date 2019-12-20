package com.example.hengestoners.main

import android.app.Application
import com.example.hengestoners.models.*
import com.example.hengestoners.models.FirebaseDB.UsersStore
import com.example.hengestoners.models.JSON.UserJSONStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MainApp : Application(), AnkoLogger {

    // lateinit var hillForts: HillFortStore
    lateinit var users: UserStore
    lateinit var signedInUser: UserModel

    override fun onCreate() {
        super.onCreate()
        users = UsersStore(applicationContext)
        info("HengeStoners started")
    }
}