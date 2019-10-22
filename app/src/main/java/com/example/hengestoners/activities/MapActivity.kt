package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class MapActivity : AppCompatActivity(), OnMapReadyCallback, AnkoLogger {

    private lateinit var map: GoogleMap
    var lat = 52.245690
    var long = -7.139102
    var hillFort = HillFortModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        hillFort = intent.extras?.getParcelable<HillFortModel>("hillFort")!!

        when(hillFort!= null){
            hillFort.location["lat"]!! <= 90 -> lat = hillFort.location["lat"]!!
            hillFort.location["long"]!! <= 180 -> long = hillFort.location["long"]!!
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val location = LatLng(lat, long)
        var title = "Unnamed"
        var snip = location.toString()
        info(hillFort)
        when(hillFort.title != null ){
            hillFort.title.isNotEmpty() -> title = hillFort.title
        }
        when(hillFort.location != null){
            hillFort.description.isNotEmpty() -> snip = hillFort.description
        }

        // Add a marker in Sydney and move the camera
        val options = MarkerOptions()
            .title(title)
            .snippet(snip)
            .draggable(true)
            .position(location)
        map.addMarker(options)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
