package com.example.hengestoners.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.activity_map.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class MapActivity : AppCompatActivity(), OnMapReadyCallback, AnkoLogger, GoogleMap.OnMarkerDragListener {

    private lateinit var map: GoogleMap
    var lat = 52.245690
    var long = -7.139102
    var hillFort = HillFortModel()

    override fun onMarkerDragEnd(marker: Marker) {
        hillFort.location["lat"] = marker.position.latitude
        hillFort.location["long"] = marker.position.longitude
        val location = LatLng(hillFort.location["lat"]!!, hillFort.location["long"]!!)
        marker.snippet = location.toString()
    }

    override fun onMarkerDragStart(marker: Marker?) {
    }

    override fun onMarkerDrag(marker: Marker?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        hillFort = intent.extras?.getParcelable("hillFort")!!

        when(hillFort!= null){
            hillFort.location["lat"]!! <= 90 -> lat = hillFort.location["lat"]!!
            hillFort.location["long"]!! <= 180 -> long = hillFort.location["long"]!!
        }
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        saveLocation.setOnClickListener{
            val resultIntent = Intent()
            resultIntent.putExtra("hillFort", hillFort)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val location = LatLng(lat, long)
        var title = "Unnamed"
        info(hillFort)
        when(hillFort.title != null ){
            hillFort.title.isNotEmpty() -> title = hillFort.title
        }

        // Add a marker in Sydney and move the camera
        val options = MarkerOptions()
            .title(title)
            .snippet(location.toString())
            .draggable(true)
            .position(location)
        map.addMarker(options)
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }
}
