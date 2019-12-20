package com.example.hengestoners.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import kotlin.collections.ArrayList
// Model for storing hillfort data

@Parcelize
@Entity
data class HillFortModel(@PrimaryKey(autoGenerate = true)var id: Long = 0,
                         var title: String = "",
                         var description: String = "",
                         var location: MutableMap<String, Double> = mutableMapOf("lat" to 91.0, "long" to 181.0),
                         var images: List<String> = ArrayList(),
                         var visited: Boolean = false,
                         var dateVisited: String = "",
                         var notes: List<String> = ArrayList()) : Parcelable