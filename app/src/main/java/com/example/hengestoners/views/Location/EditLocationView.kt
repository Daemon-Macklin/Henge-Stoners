package com.example.hengestoners.views.Location

import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_map.*
import kotlinx.android.synthetic.main.activity_map_view.*
import org.jetbrains.anko.AnkoLogger

// EditLocationView - Mostly generated activity for handling the google maps location activity
class EditLocationView : BaseView(), OnMapReadyCallback, AnkoLogger, GoogleMap.OnMarkerDragListener {

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
        presenter = initPresenter(EditLocationPresenter(this)) as EditLocationPresenter

        super.init(toolbar2, true)


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
        presenter.onMapReady(googleMap, this)
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
