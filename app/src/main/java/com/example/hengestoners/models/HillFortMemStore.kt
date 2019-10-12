package com.example.hengestoners.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HillFortMemStore: HillFortStore, AnkoLogger {

    val hillForts = ArrayList<HillFortModel>()

    override fun findAll(): List<HillFortModel> {
        return hillForts
    }

    override fun create(hillFort: HillFortModel) {
        hillForts.add(hillFort)
    }

    fun logAll(){
        hillForts.forEach { info("${it}") }
    }
}