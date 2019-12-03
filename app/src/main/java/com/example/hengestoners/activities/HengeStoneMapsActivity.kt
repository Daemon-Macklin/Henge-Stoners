package com.example.hengestoners.activities

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import com.example.hengestoners.R
import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_henge_stone_maps.*
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.content_henge_stone_maps.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class HengeStoneMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, AnkoLogger {

    lateinit var map: GoogleMap
    lateinit var app: MainApp

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        app = application as MainApp
        setContentView(R.layout.activity_henge_stone_maps)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            configureMap()
        }
    }

    fun configureMap(){
        map.setOnMarkerClickListener(this)
        map.uiSettings.setZoomControlsEnabled(true)

        app.signedInUser.hillForts.forEach {

            if (it.location["lat"] == 91.0) {
                info ("Unset Lat Long")
            } else {
                val loc = LatLng(it.location["lat"]!!, it.location["long"]!!)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it.id
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onMarkerClick(marker: Marker?): Boolean {
        if (marker != null) {
            val hillfort = app.users.findHillfortById(app.signedInUser, marker.tag.toString().toLong())
            hillfort_placeholder_title.text = hillfort.title
            hillfort_placeholder_description.text = hillfort.description
            val adapter = ImagePagerAdapter(hillfort.images, hillfort_placholder_viewPager.context)
            hillfort_placholder_viewPager.adapter = adapter
        }
        return false
    }
}
