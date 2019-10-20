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
        //var images: List<String> = ArrayList()
        //images +=("content://com.android.providers.media.documents/document/image%3A28")
        //hillForts.create(HillFortModel(0,"l", "y", mutableMapOf(), images, false, LocalDate.now(), ArrayList()))
        info("HengeStoners started")
    }
}