package com.example.hengestoners.views.MapView

import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_map_view.*
import org.jetbrains.anko.info

class MapViewPresenter(view: BaseView): BasePresenter(view){

     var selectedHillFort: HillFortModel? = null

    init {
        app = view.application as MainApp
    }

    fun doConfigMap(map: GoogleMap, listener: GoogleMap.OnMarkerClickListener){

        map.setOnMarkerClickListener(listener)
        map.uiSettings.setZoomControlsEnabled(true)

        val publicHillforts = app.users.getAllPublicHillforts()

        publicHillforts.forEach {

            if (it.location["lat"] == 91.0) {
                view!!.info ("Unset Lat Long")
            } else {
                val loc = LatLng(it.location["lat"]!!, it.location["long"]!!)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it
            }
        }
    }

    fun doMarkerClick(marker: Marker){
        val hillfort = marker.tag as HillFortModel
        view!!.hillfort_placeholder_title.text = hillfort.title
        view!!.hillfort_placeholder_description.text = hillfort.description
        val adapter = ImagePagerAdapter(hillfort.images, view!!.hillfort_placholder_viewPager.context)
        view!!.hillfort_placholder_viewPager.adapter = adapter
        selectedHillFort = hillfort
    }

    fun doToHillFort(){
        if(selectedHillFort != null)
        view!!.navigateTo(VIEW.HILLFORT, 0, "hillFort_edit", selectedHillFort)
    }
}
