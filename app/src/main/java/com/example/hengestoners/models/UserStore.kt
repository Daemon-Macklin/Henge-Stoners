package com.example.hengestoners.models

interface UserStore {
    fun findAll (): List<UserModel>
    fun create(user: UserModel): Boolean
    fun updateDetails(user: UserModel, email: String, userName: String): Boolean
    fun updatePassword(user: UserModel, curPass: String, newPass: String): Boolean
    fun remove(user: UserModel, pass: String): Boolean
    fun login(email: String, pass: String): Boolean
    fun logAll()
    fun findByEmail(email: String): UserModel?
    fun findAllHillForts(user: UserModel): List<HillFortModel>
    fun createHillFort(user: UserModel, hillFort: HillFortModel)
    fun updateHillFort(user: UserModel, hillFort: HillFortModel)
    fun logAllHillForts(user: UserModel)
    fun removeHillFort(user: UserModel, hillFort: HillFortModel)
    fun findHillfortById(user: UserModel, id: Long): HillFortModel
    fun getAllPublicHillforts(): List<HillFortModel>
    fun findUserByHillfort(hillFort: HillFortModel): UserModel?
    fun updateRating(hillFort: HillFortModel, rating: Double)
    fun getAllFavourites(user: UserModel): List<HillFortModel>
    fun findAllHillfortsById(id: Long): HillFortModel?
    fun updateUser(user: UserModel)
}