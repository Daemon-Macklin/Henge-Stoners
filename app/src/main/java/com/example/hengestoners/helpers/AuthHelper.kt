package com.example.hengestoners.helpers

import com.example.hengestoners.models.UserModel
import java.security.*

// Create a list of characters for salt generation
private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

// Function to generate a random salt
fun generateSalt(): String{
    var salt = ""
    for(i in 1..16){
        salt += charPool[(charPool.indices).random()]
    }
    return salt
}

// Function to encrypt a user password using SHA-256 encryption
fun encryptPassword(pass: String, salt: String): String {
    val HEX_CHARS = salt
    // Encrypt Password
    val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(pass.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    // Add salt to encrypted data
    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    // Return the encrypted data
    return result.toString()
}

// Method for user authentication
fun userAuth(userModel: UserModel, pass: String): Boolean {
    // Make a new hash from the attempted password and the users salt
    val hash = encryptPassword(pass, userModel.salt)

    // Return the result of comparing the new hash to the stored hash
    return userModel.password == hash
}