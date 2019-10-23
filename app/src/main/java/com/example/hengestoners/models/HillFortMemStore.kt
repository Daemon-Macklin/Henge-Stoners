package com.example.hengestoners.models

import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info



class HillFortMemStore: HillFortStore, AnkoLogger {

    val hillForts = ArrayList<HillFortModel>()

    override fun findAll(): List<HillFortModel> {
        return hillForts
    }

    override fun create(hillFort: HillFortModel) {
        hillFort.id = generateRandomUserId()
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
            foundHillFort.notes = hillFort.notes
            foundHillFort.images = hillFort.images
            logAll()
        }
    }

    override fun logAll(){
        hillForts.forEach { info("$it") }
    }

    override fun remove(hillFort: HillFortModel) {
        val index = hillForts.indexOf(hillFort)
        hillForts.removeAt(index)
    }
}