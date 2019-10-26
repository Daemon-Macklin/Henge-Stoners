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

// MapActivity - Mostly generated activity for handling the google maps location activity
class MapActivity : AppCompatActivity(), OnMapReadyCallback, AnkoLogger, GoogleMap.OnMarkerDragListener {

    private lateinit var map: GoogleMap
    var lat = 52.245690
    var long = -7.139102
    var hillFort = HillFortModel()

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        // Get the hillfort object from the extras
        hillFort = intent.extras?.getParcelable("hillFort")!!


        // If lat and long are the default values use the lat long of wit
        // If they are not the default values use them
        when(hillFort!= null){
            hillFort.location["lat"]!! <= 90 -> lat = hillFort.location["lat"]!!
            hillFort.location["long"]!! <= 180 -> long = hillFort.location["long"]!!
        }

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Function for floating button to
        saveLocation.setOnClickListener{

            // When pressed the hillfort object will be returned with a result ok
            val resultIntent = Intent()
            resultIntent.putExtra("hillFort", hillFort)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    // Method for google maps settings
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

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
        map.setOnMarkerDragListener(this)
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))
    }

    // Function to handle saving data when the marker is but down
    override fun onMarkerDragEnd(marker: Marker) {

        // Set the location data in the hillfort object
        hillFort.location["lat"] = marker.position.latitude
        hillFort.location["long"] = marker.position.longitude

        // Update the snippet to the new location
        val location = LatLng(hillFort.location["lat"]!!, hillFort.location["long"]!!)
        marker.snippet = location.toString()
    }

    override fun onMarkerDragStart(marker: Marker?) {
    }

    override fun onMarkerDrag(marker: Marker?) {
    }
}
