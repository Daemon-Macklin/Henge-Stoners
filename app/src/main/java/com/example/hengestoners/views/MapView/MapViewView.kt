package com.example.hengestoners.views.MapView

import android.os.Bundle
import android.view.MotionEvent
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Navigation.NavigationPresenter
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import kotlinx.android.synthetic.main.activity_hillfort_list.*

import kotlinx.android.synthetic.main.activity_map_view.*
import kotlinx.android.synthetic.main.activity_map_view.toolbar
import kotlinx.android.synthetic.main.content_map_view.*
import kotlinx.android.synthetic.main.content_nav_bar.*
import org.jetbrains.anko.AnkoLogger

class MapViewView : BaseView(), GoogleMap.OnMarkerClickListener, AnkoLogger {

    lateinit var presenter: MapViewPresenter
    lateinit var nagivation: NavigationPresenter
    var x1 = 0.toFloat()
    var x2 = 0.toFloat()
    val MIN_DISTANCE = 150

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var map: GoogleMap
        presenter = initPresenter(MapViewPresenter(this)) as MapViewPresenter
        nagivation = initPresenter(NavigationPresenter(this)) as NavigationPresenter
        setContentView(R.layout.activity_map_view)
        super.init(toolbar, true)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigMap(map, this, "0")
        }

        mapViewSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(!isChecked){
                mapViewSwitch.setText("All Hillforts")
                presenter.doConfigMap(map, this, "0")
            } else {
                mapViewSwitch.setText("Favourites")
                presenter.doConfigMap(map, this, "1")
            }
        }

        clearSearch.setOnClickListener {
            presenter.doConfigMap(map, this, "0")
            mapViewSwitch.isChecked = false
        }

        actionButton.setOnClickListener {
            presenter.doAction()
        }

        favButton.setOnClickListener {
            presenter.addFavourite()
        }

        copyButton.setOnClickListener {
            presenter.doCopy()
        }

        openSearch.setOnClickListener {
            presenter.openSearch()
        }

        // Set the nav maps button to be false as we are already here
        MapsActivityButton.isEnabled = false

        // Function to handle pressing the settings button
        SettingsButton.setOnClickListener() {
            nagivation.toSettings()
        }

        HomeButton.setOnClickListener() {
            nagivation.toHome()
        }

        LogOutButton.setOnClickListener() {
            nagivation.toLogOut()
        }

        navToolBar.setOnTouchListener { v, event ->
            when(event.action){
                MotionEvent.ACTION_DOWN -> x1 = event.getX()
                MotionEvent.ACTION_UP -> {
                    x2 = event.getX()
                    val deltaX = x2 - x1
                    if (Math.abs(deltaX) > MIN_DISTANCE) {
                        if(x2 > x1){
                            // Left to Right
                            nagivation.toSettings()
                        }else{
                            // Right to Left
                            nagivation.toHome()
                        }
                    }
                }
            }
            true
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
            presenter.doMarkerClick(marker)
        }
        return false
    }
}
