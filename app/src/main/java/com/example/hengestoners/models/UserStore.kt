package com.example.hengestoners.models

interface UserStore {
    fun findAll (): List<UserModel>
    fun create(user: UserModel)
    fun update(user: UserModel)
    fun remove(user: UserModel)
    fun login(email: String, pass: String): Boolean
    fun logAll()
    fun findByEmail(email: String): UserModel
    fun findAllHillForts(user: UserModel): List<HillFortModel>
    fun createHillFort(user: UserModel, hillFort: HillFortModel)
    fun updateHillFort(user: UserModel, hillFort: HillFortModel)
    fun logAllHillForts(user: UserModel)
    fun removeHillFort(user: UserModel, hillFort: HillFortModel)

}