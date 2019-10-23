package com.example.hengestoners.helpers

import com.example.hengestoners.models.UserModel
import java.security.*

private val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

fun generateSalt(): String{
    var salt = ""

    for(i in 1..16){
        salt += charPool[(charPool.indices).random()]
    }
    return salt
}

fun encryptPassword(pass: String, salt: String): String {
    val HEX_CHARS = salt
    val bytes = MessageDigest
        .getInstance("SHA-256")
        .digest(pass.toByteArray())
    val result = StringBuilder(bytes.size * 2)

    bytes.forEach {
        val i = it.toInt()
        result.append(HEX_CHARS[i shr 4 and 0x0f])
        result.append(HEX_CHARS[i and 0x0f])
    }

    return result.toString()
}

fun userAuth(userModel: UserModel, pass: String): Boolean {
    val hash = encryptPassword(pass, userModel.salt)
    return userModel.password == hash
}