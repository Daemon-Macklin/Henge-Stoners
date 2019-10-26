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

fun generateRandomId(): Long {
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
        return users
    }

    override fun create(user: UserModel): Boolean {
        if(findByEmail(user.email) == null) {
            user.id = generateRandomId()
            user.password = encryptPassword(user.password, user.salt)
            info(user)
            users.add(user)
            serialize()
            return true
        }
        return false
    }

    override fun updateDetails(user: UserModel, email: String, userName: String): Boolean {
        if(findByEmail(email) == null) {
            val foundUser: UserModel? = users.find { p -> p.id == user.id }
            if (foundUser != null) {
                foundUser.userName = userName
                foundUser.email = email
                serialize()
                return true
            }
        }
        return false
    }

    override fun updatePassword(user: UserModel, curPass: String, newPass: String): Boolean {
        val foundUser: UserModel? = users.find { p -> p.id == user.id }
        if(foundUser != null) {
            val result = userAuth(user, curPass)
            return if (result) {
                user.password = encryptPassword(newPass, user.salt)
                serialize()
                true
            } else {
                false
            }
        }
        return false
    }

    override fun remove(user: UserModel, pass: String): Boolean {
        val foundUser: UserModel? = users.find { foundUser -> foundUser.id == user.id }
        if(foundUser != null && userAuth(user, pass)) {
            users.remove(foundUser)
            serialize()
            return true
        }
        return false
    }

    override fun login(email: String, pass: String): Boolean {
        val user: UserModel? = users.find { user -> user.email == email }
        if(user != null && userAuth(user, pass)) {
            return true
        }
        return false
    }

    override fun findByEmail(email: String): UserModel? {
        return users.find { user -> user.email == email }
    }

    override fun logAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllHillForts(user: UserModel): List<HillFortModel> {
        return users.find { userModel: UserModel ->  userModel == user }!!.hillForts
    }

    override fun createHillFort(user: UserModel, hillFort: HillFortModel) {
        hillFort.id = generateRandomId()
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