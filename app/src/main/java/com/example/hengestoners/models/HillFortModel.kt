package com.example.hengestoners.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import kotlin.collections.ArrayList
// Model for storing hillfort data
@Parcelize
data class HillFortModel(var id: Long = 0,
                         var fbId: String = "",
                         var title: String = "",
                         var description: String = "",
                         var location: MutableMap<String, Double> = mutableMapOf("lat" to 91.0, "long" to 181.0),
                         var images: List<String> = ArrayList(),
                         var visited: Boolean = false,
                         var dateVisited: String = "",
                         var public: Boolean = false,
                         var rating: Double = 2.5,
                         var numberOfRatings: Int = 0,
                         var notes: List<String> = ArrayList()) : Parcelable
