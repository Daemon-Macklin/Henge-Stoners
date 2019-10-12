package com.example.hengestoners.models

interface HillFortStore {
    fun findAll (): List<HillFortModel>
    fun create(hillFort: HillFortModel)
}
