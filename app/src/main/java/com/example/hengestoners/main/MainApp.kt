package com.example.hengestoners.main

import android.app.Application
import com.example.hengestoners.models.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate

class MainApp : Application(), AnkoLogger {

    // lateinit var hillForts: HillFortStore
    lateinit var users: UserStore
    lateinit var signedInUser: UserModel

    override fun onCreate() {
        super.onCreate()
        users = UserJSONStore(applicationContext)
        info("HengeStoners started")
    }
}