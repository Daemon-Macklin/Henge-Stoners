package com.example.hengestoners.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

var lastId = 0L


internal fun getId(): Long {
    return lastId++
}

class HillFortMemStore: HillFortStore, AnkoLogger {

    val hillForts = ArrayList<HillFortModel>()

    override fun findAll(): List<HillFortModel> {
        return hillForts
    }

    override fun create(hillFort: HillFortModel) {
        hillFort.id = getId()
        hillForts.add(hillFort)
    }

    override fun update(hillFort: HillFortModel){
        var foundHillFort: HillFortModel? = hillForts.find { p -> p.id == hillFort.id }
        if (foundHillFort != null){
            foundHillFort.title = hillFort.title
            foundHillFort.description = hillFort.description
            foundHillFort.location["lat"] = hillFort.location["lat"].toString().toDouble()
            foundHillFort.location["long"] = hillFort.location["long"].toString().toDouble()
            foundHillFort.visited = hillFort.visited
            foundHillFort.dateVisited = hillFort.dateVisited
            logAll()
        }
    }

    fun logAll(){
        hillForts.forEach { info("${it}") }
    }
}