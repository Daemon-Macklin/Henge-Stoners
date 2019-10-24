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

fun myHillFortTotal(user: UserModel): Int{
    return user.hillForts.size
}

