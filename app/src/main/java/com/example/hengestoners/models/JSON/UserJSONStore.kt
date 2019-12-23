package com.example.hengestoners.models.JSON

import android.content.Context
import com.example.hengestoners.helpers.*
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.models.UserStore
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.util.*
import kotlin.collections.ArrayList

// Functions to manage users and their hillforts


val JSON_FILE = "hillFortData.json"
val gsonBuilder = GsonBuilder().setPrettyPrinting().create()
val listType = object : TypeToken<java.util.ArrayList<UserModel>>() {}.type


// Function to randomly generate id for users or hillforts
fun generateRandomId(): Long {
    return Random().nextLong()
}

class UserJSONStore: UserStore, AnkoLogger {
    override fun updateUser(user: UserModel) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllFavourites(user: UserModel): List<HillFortModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findAllHillfortsById(id: Long): HillFortModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateRating(hillFort: HillFortModel, rating: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun findUserByHillfort(hillFort: HillFortModel): UserModel? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllPublicHillforts(): List<HillFortModel> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    val context: Context
    var users = mutableListOf<UserModel>()

    // When created see if json file exists and load it
    constructor(context: Context) {
        this.context = context
        if(exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    // Method to find all users
    override fun findAll(): List<UserModel> {
        return users
    }

    // Method to create a new user
    override fun create(user: UserModel): Boolean {
        // Check if the email is already used
        if(findByEmail(user.email) == null) {
            // If not add the user, save the data and return true
            user.id = generateRandomId()
            user.password = encryptPassword(user.password, user.salt)
            // user.hillForts = addDefaultHillforts()
            info(user)
            users.add(user)
            serialize()
            return true
        }
        // else return false
        return false
    }

    // Method to update user email or password
    override fun updateDetails(user: UserModel, email: String, userName: String): Boolean {
        // Check if the email is already in use
        if(findByEmail(email) == null) {

            // If it is not then find
            val foundUser: UserModel? = users.find { p -> p.id == user.id }
            if (foundUser != null) {
                // If the user is found update, save and return true
                foundUser.userName = userName
                foundUser.email = email
                serialize()
                return true
            }
        }

        // Else return false
        return false
    }

    // Method to update the user password
    override fun updatePassword(user: UserModel, curPass: String, newPass: String): Boolean {

        // Find the user
        val foundUser: UserModel? = users.find { p -> p.id == user.id }
        if(foundUser != null) {

            // If found verify the password
            val result = userAuth(user, curPass)

            return if (result) {

                // If the password is verified then set the encrypted password as the new user password
                // save and return true
                user.password = encryptPassword(newPass, user.salt)
                serialize()
                true
            } else {
                // else return false
                false
            }
        }
        // Return false
        return false
    }

    // Function to remove user
    override fun remove(user: UserModel, pass: String): Boolean {
        // Find the user
        val foundUser: UserModel? = users.find { foundUser -> foundUser.id == user.id }
        if(foundUser != null && userAuth(user, pass)) {

            // If the user is found and the password is verified remove the user, save and return true
            users.remove(foundUser)
            serialize()
            return true
        }

        // Else return false
        return false
    }


    // Funtion to login user
    override fun login(email: String, pass: String): Boolean {
        // Find the user
        val user = findByEmail(email)

        if(user != null && userAuth(user, pass)) {

            // If the user is found and the password verified then return true
            return true
        }

        // Else return false
        return false
    }

    // Function to find user by email address
    override fun findByEmail(email: String): UserModel? {
        return users.find { user -> user.email == email }
    }

    // Function to log all users
    override fun logAll() {
        users.forEach {
            info(it)
        }
    }

    // Function to find all a given users fillforts
    override fun findAllHillForts(user: UserModel): List<HillFortModel> {
        return users.find { userModel: UserModel ->  userModel == user }!!.hillForts
    }

    override fun findHillfortById(user: UserModel, id: Long): HillFortModel {
        return user.hillForts.find { hillFortModel: HillFortModel -> hillFortModel.id == id }!!
    }

    // Function to create a new hillfort
    override fun createHillFort(user: UserModel, hillFort: HillFortModel) {

        // Give hillfort id, add to users list then save
        hillFort.id = generateRandomId()
        user.hillForts.add(hillFort)
        serialize()
    }

    // Function to update a hillfort
    override fun updateHillFort(user: UserModel, hillFort: HillFortModel){

        // Find the hillfort
        var foundHillFort: HillFortModel? = user.hillForts.find { p -> p.id == hillFort.id }

        if (foundHillFort != null){
            // If the hillfort it found. update and save
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

    // Function to log all hillforts
    override fun logAllHillForts(user: UserModel){
        user.hillForts.forEach { info("$it") }
    }

    // Function to remove hillfort
    override fun removeHillFort(user: UserModel, hillFort: HillFortModel) {
        user.hillForts.remove(hillFort)
        serialize()
    }

    /*
    // Function to add default hillforts to user
    private fun addDefaultHillforts(): ArrayList<HillFortModel> {
        var defaultHillforts = ArrayList<HillFortModel>()
        val default1 = HillFortModel(
            generateRandomId(),
            "BallinKillin",
            "",
            mutableMapOf("lat" to 52.6540, "long" to -6.9313),
            ArrayList(),
            false,
            "",
            ArrayList()
        )
        val default2 = HillFortModel(
            generateRandomId(),
            "Crag",
            "",
            mutableMapOf("lat" to 52.92804, "long" to -9.34815),
            ArrayList(),
            false,
            "",
            ArrayList()
        )
        val default3 = HillFortModel(
            generateRandomId(),
            "Woodstown",
            "",
            mutableMapOf("lat" to 52.13787, "long" to -7.27012),
            ArrayList(),
            false,
            "",
            ArrayList()
        )
        defaultHillforts.add(default1)
        defaultHillforts.add(default2)
        defaultHillforts.add(default3)
        return defaultHillforts
    }

     */

    // Function to write data to the json file
    private fun serialize() {
        val jsonString = gsonBuilder.toJson(users,
            listType
        )
        write(context, JSON_FILE, jsonString)
    }

    // Function to read data from json file
    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        users = Gson().fromJson(jsonString, listType)
    }

}