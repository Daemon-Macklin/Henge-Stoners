package com.example.hengestoners.main

import android.app.Application
import com.example.hengestoners.models.HillFortJSONStore
import com.example.hengestoners.models.HillFortMemStore
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.HillFortStore
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.time.LocalDate

class MainApp : Application(), AnkoLogger {

    lateinit var hillForts: HillFortStore

    override fun onCreate() {
        super.onCreate()
        hillForts = HillFortJSONStore(applicationContext)
        info("HengeStoners started")
    }
}