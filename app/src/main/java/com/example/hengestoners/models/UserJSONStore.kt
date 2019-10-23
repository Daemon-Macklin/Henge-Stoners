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
import java.lang.Exception
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

    override fun findAll(): List<UserModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun create(user: UserModel) {
        user.id = generateRandomUserId()
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
        if (pass == user.email){
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

    constructor(context: Context) {
        this.context = context
        if(exists(context, JSON_FILE)) {
            deserialize()
        }
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