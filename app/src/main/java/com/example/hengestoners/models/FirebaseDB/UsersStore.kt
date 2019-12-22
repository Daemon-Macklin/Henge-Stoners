package com.example.hengestoners.models.FirebaseDB

import android.content.Context
import android.graphics.Bitmap
import androidx.constraintlayout.solver.widgets.Snapshot
import androidx.core.graphics.get
import com.example.hengestoners.helpers.*
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.models.UserStore
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

// Functions to manage users and their hillforts


lateinit var db: DatabaseReference
lateinit var st: StorageReference
var imagesCount: Int = 0

// Function to randomly generate id for users or hillforts
fun generateRandomId(): Long {
    return Random().nextLong()
}

class UsersStore// When created see if json file exists and load it
    (val context: Context) : UserStore, AnkoLogger {

    var users = mutableListOf<UserModel>()


    init {
        fetchUsers{}
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
            val key = db.child("users").push().key
            key?.let {
                user.fbId = key
                users.add(user)
                db.child("users").child(key).setValue(user)
            }
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
                db.child("users").child(user.fbId).setValue(user)
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
                db.child("users").child(user.fbId).setValue(user)
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
            db.child("users").child(user.fbId).removeValue()
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
        hillFort.images = hillFort.images
        val key = db.child("users").child(user.fbId).child("hillForts").push().key
        key?.let {
            hillFort.fbId = key
            user.hillForts.add(hillFort)
            db.child("users").child(user.fbId).child("hillForts").setValue(user.hillForts)
        }
        uploadImage(user, hillFort)
    }

    // Function to update a hillfort
    override fun updateHillFort(user: UserModel, hillFort: HillFortModel) {

        // Find the hillfort
        var foundHillFort: HillFortModel? = user.hillForts.find { p -> p.id == hillFort.id }

        if (foundHillFort != null) {
            // If the hillfort it found. update and save
            foundHillFort.title = hillFort.title
            foundHillFort.description = hillFort.description
            foundHillFort.location["lat"] = hillFort.location["lat"].toString().toDouble()
            foundHillFort.location["long"] = hillFort.location["long"].toString().toDouble()
            foundHillFort.visited = hillFort.visited
            foundHillFort.dateVisited = hillFort.dateVisited
            foundHillFort.notes = hillFort.notes
            hillFort.images = hillFort.images
            logAllHillForts(user)
            db.child("users").child(user.fbId).child("hillForts").setValue(user.hillForts)

            uploadImage(user, hillFort)
            // val query = db.child("users").child(user.fbId).child("hillForts").orderByChild("id").equalTo(hillFort.id.toString())
        }
    }

    // Function to log all hillforts
    override fun logAllHillForts(user: UserModel){
        user.hillForts.forEach { info("$it") }
    }

    // Function to remove hillfort
    override fun removeHillFort(user: UserModel, hillFort: HillFortModel) {
        user.hillForts.remove(hillFort)
        db.child("users").child(user.fbId).child("hillForts").orderByChild("id").equalTo(hillFort.id.toString()).ref.removeValue()
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

    fun uploadImage(user: UserModel, hillFort: HillFortModel) {

        hillFort.images.forEach {
            if (it  != "" || it != "content://") {
                val fileName = File(it)
                val imageName = fileName.getName()

                var imageRef = st.child(user.fbId + '/' + imageName)
                val baos = ByteArrayOutputStream()
                val bitmap = readImageFromPath(context, it)

                bitmap?.let {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                    val data = baos.toByteArray()
                    val uploadTask = imageRef.putBytes(data)
                    val addOnSuccessListener: Any = uploadTask.addOnFailureListener {
                        info(it.message)
                    }.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot ->
                        taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {

                            db.child("users").child(user.fbId).child("hillForts").setValue(user.hillForts)
                        }
                    }
                    addOnSuccessListener
                }
            }
            imagesCount += 1
        }
    }

    fun fetchUsers(usersReady: () -> Unit) {
        val valueEventListener = object : ValueEventListener {
            override fun onCancelled(dataSnapshot: DatabaseError) {
            }
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                dataSnapshot!!.children.mapNotNullTo(users) { it.getValue<UserModel>(UserModel::class.java) }
                usersReady()
            }
        }
        db = FirebaseDatabase.getInstance().reference
        st = FirebaseStorage.getInstance().reference
        db.child("users").addListenerForSingleValueEvent(valueEventListener)
    }
}

