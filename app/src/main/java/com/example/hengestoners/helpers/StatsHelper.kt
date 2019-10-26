package com.example.hengestoners.helpers

import com.example.hengestoners.models.UserModel
// Function to calculate stats

// A total of all the hillforts across all users
fun hillFortTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach{
        total += it.hillForts.size
    }
    return total
}

// A total of all the hillforts visited across all users
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

// A total of all hillforts visited by signed in user
fun myHillFortVisited(user: UserModel): Int {
    var total = 0
    user.hillForts.forEach {
        if(it.visited){
            total += 1
        }
    }
    return total
}

// Total of hillforts by signed in user
fun myHillFortTotal(user: UserModel): Int{
    return user.hillForts.size
}

// A total of all the notes across all users
fun notesTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach{userModel ->
        userModel.hillForts.forEach{
            total += it.notes.size
        }
    }
    return total
}

// A total of all the notes for the signed in user
fun myNotesTotal(user: UserModel): Int{
    var total = 0
    user.hillForts.forEach{
        total += it.notes.size
    }
    return total
}

// A total of all the images across all users
fun imageTotal(users: List<UserModel>): Int{
    var total = 0
    users.forEach { userModel ->
        userModel.hillForts.forEach{
            total += it.images.size
        }
    }
    return total
}

// A total of images of the signed in users
fun myImageTotal(user: UserModel): Int{
    var total = 0
    user.hillForts.forEach {
        total += it.images.size
    }
    return total
}

// The user with the most hillforts
fun userWithMostHillforts(users: List<UserModel>): String{
    var result = ""
    var most = 0
    users.forEach{
        if(it.hillForts.size > most){
            result = it.userName
        }
    }
    return result
}

// The user who has visited the most hillforts
fun userWithMostVisitedHillForts(users: List<UserModel>): String{
    var result = ""
    var most = 0
    users.forEach {user: UserModel ->
        var visted = 0
        user.hillForts.forEach {
            if(it.visited){
                visted +=1
            }
        }
        if(visted > most){
            result = user.userName
        }
    }
    return result
}


