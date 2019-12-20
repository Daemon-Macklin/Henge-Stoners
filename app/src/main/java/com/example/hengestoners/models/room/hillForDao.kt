package com.example.hengestoners.models.room

import androidx.room.*
import com.example.hengestoners.models.HillFortModel

@Dao
interface hillForDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(hillfort: HillFortModel)

    @Query("SELECT * FROM HillFortModel")
    fun findAll(): List<HillFortModel>

    @Query("select * from HillFortModel where id = :id")
    fun findById(id: Long): HillFortModel

    @Update
    fun update(placemark: HillFortModel)

    @Delete
    fun deletePlacemark(placemark: HillFortModel)
}