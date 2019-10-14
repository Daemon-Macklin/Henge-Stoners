package com.example.hengestoners.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.time.LocalDate
import kotlin.collections.ArrayList

@Parcelize
data class HillFortModel(var id: Long = 0,
                         var title: String = "",
                         var description: String = "",
                         var location: MutableMap<String, Double> = mutableMapOf("lat" to 91.0, "long" to 181.0),
                         var images: List<String> = ArrayList(),
                         var visited: Boolean = false,
                         var dateVisited: LocalDate = LocalDate.now(),
                         var notes: List<String> = ArrayList()) : Parcelable