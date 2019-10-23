package com.example.hengestoners.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import kotlin.collections.ArrayList

@Parcelize
data class UserModel(var id: Long = 0,
                         var email: String = "",
                         var password: String = "",
                         var hillForts : MutableList<HillFortModel> = mutableListOf()) : Parcelable
