package com.example.hengestoners.models

import android.content.Context
import com.example.hengestoners.helpers.exists
import com.example.hengestoners.helpers.read
import com.example.hengestoners.helpers.write
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList

val JSON_FILE = "hillForts.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<HillFortModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class HillFortJSONStore: HillFortStore, AnkoLogger {

    val context: Context
    var hillForts = mutableListOf<HillFortModel>()

    constructor(context: Context) {
        this.context = context
        if(exists(context, JSON_FILE)) {
            deserialize()
        }
    }
    override fun findAll(): List<HillFortModel> {
        return hillForts
    }

    override fun create(hillFort: HillFortModel) {
        hillFort.id = generateRandomId()
        hillForts.add(hillFort)
        serialize()
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
            serialize()
        }
    }

    override fun logAll(){
        hillForts.forEach { info("$it") }
    }

    override fun remove(hillFort: HillFortModel) {
        val index = hillForts.indexOf(hillFort)
        hillForts.removeAt(index)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(hillForts, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        hillForts = Gson().fromJson(jsonString, listType)
    }
}
