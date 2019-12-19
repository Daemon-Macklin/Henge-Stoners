package com.example.hengestoners.views.Location

import android.app.Activity
import android.content.Intent
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class EditLocationPresenter(view: BaseView): BasePresenter(view){

    private lateinit var map: GoogleMap
    var lat = 52.245690
    var long = -7.139102
    val hillFort: HillFortModel


    init {
        app = view.application as MainApp
        hillFort = view.intent.extras?.getParcelable("hillFort")!!
    }

    fun onMapReady(googleMap: GoogleMap, listener: GoogleMap.OnMarkerDragListener){
        map = googleMap

        // If lat and long are the default values use the lat long of wit
        // If they are not the default values use them
        if(hillFort.location["lat"] != 91.0) {
            lat = hillFort.location["lat"]!!
            long = hillFort.location["long"]!!
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
        map.setOnMarkerDragListener(listener)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
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
        view!!.setResult(Activity.RESULT_OK, resultIntent)
        view!!.finish()
    }
}