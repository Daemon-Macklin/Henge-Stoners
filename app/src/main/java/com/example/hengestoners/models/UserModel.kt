package com.example.hengestoners.models

import android.os.Parcelable
import com.example.hengestoners.helpers.generateSalt
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue
import kotlin.collections.ArrayList

@Parcelize
data class UserModel(var id: Long = 0,
                         var userName: String = "",
                         var email: String = "",
                         var password: String = "",
                         var salt : String = generateSalt(),
                         var hillForts : MutableList<HillFortModel> = mutableListOf()) : Parcelable
