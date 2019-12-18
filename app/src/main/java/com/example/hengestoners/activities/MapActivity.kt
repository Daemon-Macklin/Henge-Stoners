package com.example.hengestoners.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.presenters.EditLocationPresenter

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
    lateinit var presenter: EditLocationPresenter


    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        presenter = EditLocationPresenter(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Function for floating button to
        saveLocation.setOnClickListener{
            presenter.doSaveLocation()
        }
    }

    // Method for google maps settings
    override fun onMapReady(googleMap: GoogleMap) {
        presenter.onMapReady(googleMap)
    }

    // Function to handle saving data when the marker is but down
    override fun onMarkerDragEnd(marker: Marker) {
        presenter.onMarkerDragEnd(marker)
    }

    override fun onMarkerDragStart(marker: Marker?) {
        // Put this in presenter
    }

    override fun onMarkerDrag(marker: Marker?) {
        // Put this in presenter

    }
}
