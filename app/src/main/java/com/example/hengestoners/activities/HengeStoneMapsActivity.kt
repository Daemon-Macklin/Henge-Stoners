package com.example.hengestoners.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.hengestoners.R

import kotlinx.android.synthetic.main.activity_henge_stone_maps.*

class HengeStoneMapsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_henge_stone_maps)
        setSupportActionBar(toolbar)

    }

}
