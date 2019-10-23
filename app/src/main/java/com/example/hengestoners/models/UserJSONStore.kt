package com.example.hengestoners.models

import android.content.Context
import com.example.hengestoners.helpers.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.lang.Exception
import java.net.PasswordAuthentication
import java.util.*

val JSON_FILE = "hillFortData.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type

fun generateRandomUserId(): Long {
    return Random().nextLong()
}

class UserJSONStore: UserStore, AnkoLogger {

    val context: Context
    var users = mutableListOf<UserModel>()
    
    constructor(context: Context) {
        this.context = context
        if(exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(user: UserModel) {
        user.id = generateRandomUserId()
        user.password = encryptPassword(user.password, user.salt)
        info(user)
        users.add(user)
        serialize()
    }

    override fun update(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun remove(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun login(email: String, pass: String): Boolean {
        var user: UserModel
        try {
            user = users.first { user -> user.email == email }
        } catch (e: Exception){
            info (e)
            return false
        }
        if (userAuth(user, pass)){
            return true
        }
        return false
    }

    override fun findByEmail(email: String): UserModel {
        return users.first { user -> user.email == email }
    }

    override fun logAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllHillForts(user: UserModel): List<HillFortModel> {
        return users.find { user -> user == user }!!.hillForts
    }

    override fun createHillFort(user: UserModel, hillFort: HillFortModel) {
        hillFort.id = generateRandomUserId()
        user.hillForts.add(hillFort)
        serialize()
    }

    override fun updateHillFort(user: UserModel, hillFort: HillFortModel){
        var foundHillFort: HillFortModel? = user.hillForts.find { p -> p.id == hillFort.id }
        if (foundHillFort != null){
            foundHillFort.title = hillFort.title
            foundHillFort.description = hillFort.description
            foundHillFort.location["lat"] = hillFort.location["lat"].toString().toDouble()
            foundHillFort.location["long"] = hillFort.location["long"].toString().toDouble()
            foundHillFort.visited = hillFort.visited
            foundHillFort.dateVisited = hillFort.dateVisited
            foundHillFort.notes = hillFort.notes
            foundHillFort.images = hillFort.images
            logAllHillForts(user)
            serialize()
        }
    }

    override fun logAllHillForts(user: UserModel){
        user.hillForts.forEach { info("$it") }
    }

    override fun removeHillFort(user: UserModel, hillFort: HillFortModel) {
        val index = user.hillForts.indexOf(hillFort)
        user.hillForts.removeAt(index)
        serialize()
    }


    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }

}