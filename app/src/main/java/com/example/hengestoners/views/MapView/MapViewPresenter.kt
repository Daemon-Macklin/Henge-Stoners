package com.example.hengestoners.views.MapView

import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.main.MainApp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_henge_stone_maps.*
import org.jetbrains.anko.info

class MapViewPresenter(val view: HengeStoneMapsActivity){

    lateinit var app: MainApp

    init {
        app = view.application as MainApp
    }

    fun doConfigMap(map: GoogleMap){

        map.setOnMarkerClickListener(view)
        map.uiSettings.setZoomControlsEnabled(true)

        app.signedInUser.hillForts.forEach {

            if (it.location["lat"] == 91.0) {
                view.info ("Unset Lat Long")
            } else {
                val loc = LatLng(it.location["lat"]!!, it.location["long"]!!)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it.id
            }
        }
    }

    fun doMarkerClick(marker: Marker){
        val hillfort = app.users.findHillfortById(app.signedInUser, marker.tag.toString().toLong())
        view.hillfort_placeholder_title.text = hillfort.title
        view.hillfort_placeholder_description.text = hillfort.description
        val adapter = ImagePagerAdapter(hillfort.images, view.hillfort_placholder_viewPager.context)
        view.hillfort_placholder_viewPager.adapter = adapter
    }
}
