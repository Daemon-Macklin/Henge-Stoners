package com.example.hengestoners.models.room

import android.content.Context
import androidx.room.Room
import com.example.hengestoners.models.HillFortModel


class HillFortStoreRoom(val context: Context) : HillFortStore {

    var dao: hillForDao

    init {
        val database = Room.databaseBuilder(context, Database::class.java, "room_sample.db")
            .fallbackToDestructiveMigration()
            .build()
        dao = database.hillForDao()
    }

    override fun findAll(): List<HillFortModel> {
        return dao.findAll()
    }

    override fun findById(id: Long): HillFortModel? {
        return dao.findById(id)
    }

    override fun create(placemark: HillFortModel) {
        dao.create(placemark)
    }

    override fun update(placemark: HillFortModel) {
        dao.update(placemark)
    }

    override fun delete(placemark: HillFortModel) {
        dao.deletePlacemark(placemark)
    }
}
