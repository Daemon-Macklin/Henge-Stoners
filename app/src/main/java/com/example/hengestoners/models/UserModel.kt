package com.example.hengestoners.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.hengestoners.helpers.generateSalt
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlin.collections.ArrayList
// Model to store user data
@Parcelize
@Entity
data class UserModel(@PrimaryKey(autoGenerate = true)var id: Long = 0,
                     var userName: String = "",
                     var email: String = "",
                     var password: String = "",
                     var salt : String = generateSalt(),
                     var hillForts : MutableList<HillFortModel> = mutableListOf()) : Parcelable
