package com.example.hengestoners.presenters

import android.app.Activity
import android.content.Intent
import com.example.hengestoners.activities.HengeStoneMapsActivity
import com.example.hengestoners.activities.MapActivity
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.info

class EditLocationPresenter(val view: MapActivity){

    private lateinit var map: GoogleMap
    var lat = 52.245690
    var long = -7.139102
    val hillFort: HillFortModel
    var app: MainApp



    init {
        app = view.application as MainApp
        hillFort = view.intent.extras?.getParcelable("hillFort")!!
    }

    fun onMapReady(googleMap: GoogleMap){
        map = googleMap

        // If lat and long are the default values use the lat long of wit
        // If they are not the default values use them
        when(hillFort!= null){
            hillFort.location["lat"]!! <= 90 -> lat = hillFort.location["lat"]!!
            hillFort.location["long"]!! <= 180 -> long = hillFort.location["long"]!!
        }
        
        // Get the lat and long
        val location = LatLng(lat, long)

        // If the hillfort has a title use that on the map
        // If not use Unnamed
        var title = "Unnamed"
        when(hillFort.title != null ){
            hillFort.title.isNotEmpty() -> title = hillFort.title
        }

        // Add a marker in the location and move the camera
        val options = MarkerOptions()
            .title(title)
            .snippet(location.toString())
            .draggable(true)
            .position(location)
        map.addMarker(options)
        map.setOnMarkerDragListener(view)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    fun onMarkerDragEnd(marker: Marker){

        // Set the location data in the hillfort object
        hillFort.location["lat"] = marker.position.latitude
        hillFort.location["long"] = marker.position.longitude

        // Update the snippet to the new location
        val location = LatLng(hillFort.location["lat"]!!, hillFort.location["long"]!!)
        marker.snippet = location.toString()
    }

    fun doSaveLocation(){
        // When pressed the hillfort object will be returned with a result ok
        val resultIntent = Intent()
        resultIntent.putExtra("hillFort", hillFort)
        view.setResult(Activity.RESULT_OK, resultIntent)
        view.finish()
    }
}