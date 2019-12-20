package com.example.hengestoners.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.hengestoners.models.HillFortModel

@Database(entities = arrayOf(HillFortModel::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun hillForDao(): hillForDao
}