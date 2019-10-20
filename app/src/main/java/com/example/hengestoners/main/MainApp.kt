package com.example.hengestoners.main

import android.app.Application
import com.example.hengestoners.models.HillFortMemStore
import com.example.hengestoners.models.HillFortModel
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate

class MainApp : Application(), AnkoLogger {

    val hillForts = HillFortMemStore()

    override fun onCreate() {
        super.onCreate()
        info("HengeStoners started")
    }
}