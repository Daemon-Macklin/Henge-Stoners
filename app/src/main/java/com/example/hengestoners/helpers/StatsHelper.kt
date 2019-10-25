package com.example.hengestoners.helpers

import com.example.hengestoners.models.UserModel

fun hillFortTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach{
        total += it.hillForts.size
    }
    return total
}

fun hillFortVisited(users: List<UserModel>): Int{
    var total = 0
    users.forEach{ userModel ->
        userModel.hillForts.forEach{
            if(it.visited){
                total += 1
            }
        }
    }
    return total
}

fun myHillFortVisited(user: UserModel): Int {
    var total = 0
    user.hillForts.forEach {
        if(it.visited){
            total += 1
        }
    }
    return total
}

fun myHillFortTotal(user: UserModel): Int{
    return user.hillForts.size
}

fun notesTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach{userModel ->
        userModel.hillForts.forEach{
            total += it.notes.size
        }
    }
    return total
}

fun myNotesTotal(user: UserModel): Int{
    var total = 0
    user.hillForts.forEach{
        total = it.notes.size
    }
    return total
}

fun imageTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach { userModel ->
        userModel.hillForts.forEach{
            total += it.images.size
        }
    }
    return total
}

fun myImageTotal(user: UserModel): Int{
    var total = 0
    user.hillForts.forEach {
        total += it.images.size
    }
    return total
}