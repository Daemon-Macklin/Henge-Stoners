package com.example.hengestoners.views.MapView

import android.annotation.SuppressLint
import android.widget.PopupMenu
import com.example.hengestoners.R
import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.content_map_view.*
import org.jetbrains.anko.info
import kotlin.math.round

class MapViewPresenter(view: BaseView): BasePresenter(view){

     var selectedHillFort: HillFortModel? = null

    init {
        app = view.application as MainApp
    }

    fun doConfigMap(map: GoogleMap, listener: GoogleMap.OnMarkerClickListener, option: String){

        map.clear()
        map.setOnMarkerClickListener(listener)
        map.uiSettings.setZoomControlsEnabled(true)
        val publicHillforts: List<HillFortModel>
        if(option == "1") {
            publicHillforts = app.users.getAllFavourites(app.signedInUser)
        } else {
            publicHillforts = app.users.getAllPublicHillforts()
        }
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
        view!!.rating.text = "%.2f".format(hillfort.rating)
        val user = app.users.findUserByHillfort(hillfort)

        if(user != null){
            view!!.userLabel.text = user.userName
        }

        if(user == app.signedInUser){
            view!!.actionButton.setText("Go To")
        } else {
            view!!.actionButton.setText("Rate")
        }

        val adapter = ImagePagerAdapter(hillfort.images, view!!.hillfort_placholder_viewPager.context)
        view!!.hillfort_placholder_viewPager.adapter = adapter
        selectedHillFort = hillfort
    }

    fun doAction() {
        if (selectedHillFort != null) {
            if (view!!.actionButton.text.toString() == "Go To") {
                view!!.navigateTo(VIEW.HILLFORT, 0, "hillFort_edit", selectedHillFort)
            } else {
                showPopUp()
            }
        }
    }

    fun showPopUp() {
        val popupMenu = PopupMenu(view!!, view!!.cardView)
        val inflater = popupMenu.menuInflater
        inflater.inflate(R.menu.hillfort_popup, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when(it.itemId) {
                R.id.header5 -> {
                    app.users.updateRating(selectedHillFort!!, 5)
                }
                R.id.header4 -> {
                    app.users.updateRating(selectedHillFort!!, 4)
                }
                R.id.header3 -> {
                    app.users.updateRating(selectedHillFort!!, 3)
                }
                R.id.header2 -> {
                    app.users.updateRating(selectedHillFort!!, 2)
                }
                R.id.header1 -> {
                    app.users.updateRating(selectedHillFort!!, 1)
                }
            }
            true
        }
    }

    fun addFavourite() {
        if (selectedHillFort != null) {
            val id = selectedHillFort!!.id
            app.signedInUser.favouriteHillforts.forEach {
                if(it == id){
                    return
                }
            }
            app.signedInUser.favouriteHillforts += id
            app.users.updateUser(app.signedInUser)
        }
    }
}
