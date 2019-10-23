package com.example.hengestoners.models

interface UserStore {
    fun findAll (): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun remove(user: UserModel)
    fun login(email: String, pass: String): Boolean
    fun logAll()
    fun findByEmail(email: String): UserModel
}