package com.example.hengestoners.views.MapView

import android.os.Bundle
import com.example.hengestoners.R
import com.example.hengestoners.views.Base.BaseView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.activity_map_view.*
import kotlinx.android.synthetic.main.content_map_view.*
import org.jetbrains.anko.AnkoLogger

class MapViewView : BaseView(), GoogleMap.OnMarkerClickListener, AnkoLogger {

    lateinit var presenter: MapViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var map: GoogleMap

        presenter = initPresenter(MapViewPresenter(this)) as MapViewPresenter

        setContentView(R.layout.activity_map_view)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigMap(map, this)
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
