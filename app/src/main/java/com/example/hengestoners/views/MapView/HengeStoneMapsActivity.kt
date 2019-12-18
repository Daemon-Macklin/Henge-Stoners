package com.example.hengestoners.views.MapView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.hengestoners.R
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker

import kotlinx.android.synthetic.main.activity_henge_stone_maps.*
import kotlinx.android.synthetic.main.content_henge_stone_maps.*
import org.jetbrains.anko.AnkoLogger

class HengeStoneMapsActivity : AppCompatActivity(), GoogleMap.OnMarkerClickListener, AnkoLogger {

    lateinit var presenter: MapViewPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lateinit var map: GoogleMap

        presenter = MapViewPresenter(this)
        setContentView(R.layout.activity_henge_stone_maps)
        setSupportActionBar(toolbar)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync {
            map = it
            presenter.doConfigMap(map)
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
