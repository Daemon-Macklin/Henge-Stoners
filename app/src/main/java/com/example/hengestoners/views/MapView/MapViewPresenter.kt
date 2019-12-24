package com.example.hengestoners.views.MapView

import android.annotation.SuppressLint
import android.media.Rating
import android.view.ViewGroup
import android.widget.*
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
import kotlinx.android.synthetic.main.activity_hengestoners.*
import kotlinx.android.synthetic.main.content_map_view.*
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import kotlin.math.round

class MapViewPresenter(view: BaseView): BasePresenter(view) {

    var selectedHillFort: HillFortModel? = null
    var publicHillforts = app.users.getAllPublicHillforts()
    lateinit var map: GoogleMap
    lateinit var listener: GoogleMap.OnMarkerClickListener

    init {
        app = view.application as MainApp
    }

    fun doConfigMap(map: GoogleMap, listener: GoogleMap.OnMarkerClickListener, option: String) {
        this.map = map
        this.listener = listener

        map.clear()
        map.setOnMarkerClickListener(listener)
        map.uiSettings.setZoomControlsEnabled(true)
        if (option == "1") {
            publicHillforts = app.users.getAllFavourites(app.signedInUser)
        } else if (option == "0"){
            publicHillforts = app.users.getAllPublicHillforts()
        }

        publicHillforts.forEach {

            if (it.location["lat"] == 91.0) {
                view!!.info("Unset Lat Long")
            } else {
                val loc = LatLng(it.location["lat"]!!, it.location["long"]!!)
                val options = MarkerOptions().title(it.title).position(loc)
                map.addMarker(options).tag = it
            }
        }
    }


    fun doMarkerClick(marker: Marker) {
        val hillfort = marker.tag as HillFortModel
        view!!.hillfort_placeholder_title.text = hillfort.title
        view!!.hillfort_placeholder_description.text = hillfort.description
        view!!.rating.text = "%.2f".format(hillfort.rating)

        if(app.users.isFavourite(app.signedInUser, hillfort)){
            view!!.favButton.text = "UnFavourite"
        } else {
            view!!.favButton.text = "Favourite"
        }

        if(hillfort.location["lat"]!! <= 90) {
            val str = "lat = " + "%.4f".format(hillfort.location["lat"]) + "\nLong = " + "%.4f".format(hillfort.location["long"])
            view!!.hillfortLocal.text = str
        }

        val user = app.users.findUserByHillfort(hillfort)

        if (user != null) {
            view!!.userLabel.text = user.userName
        }

        if (user == app.signedInUser) {
            view!!.actionButton.setText("Go To")
        } else {
            view!!.actionButton.setText("Rate")
        }

        val adapter =
            ImagePagerAdapter(hillfort.images, view!!.hillfort_placholder_viewPager.context)
        view!!.hillfort_placholder_viewPager.adapter = adapter
        selectedHillFort = hillfort
    }

    fun doAction() {
        if (selectedHillFort != null) {
            if (view!!.actionButton.text.toString() == "Go To") {
                view!!.navigateTo(VIEW.HILLFORT, 0, "hillFort_edit", selectedHillFort)
            } else {
                showRatingPopUp()
            }
        }
    }

    fun showRatingPopUp() {
        val window = PopupWindow(view!!)
        val popUp = view!!.layoutInflater.inflate(R.layout.rating_popup, null)
        window.contentView = popUp
        window.showAtLocation(view!!.cardView, 10, 250, 250)

        view!!.actionButton.isClickable = false
        val ratingBar = popUp.findViewById<RatingBar>(R.id.popUpRatingBar)
        val submitButton = popUp.findViewById<Button>(R.id.popUpSubmit)
        val cancelButton = popUp.findViewById<Button>(R.id.popUpCancel)

        submitButton.setOnClickListener {
            var rating = ratingBar.rating.toDouble()
            app.users.updateRating(selectedHillFort!!, rating)
            window.dismiss()
            view!!.actionButton.isClickable = true
        }

        cancelButton.setOnClickListener {
            window.dismiss()
            view!!.actionButton.isClickable = true
        }

    }

    fun addFavourite() {
        if (selectedHillFort != null) {
            val id = selectedHillFort!!.id
            app.signedInUser.favouriteHillforts.forEach {
                if (it == id) {
                    view!!.toast("Already in Your Favourites!")
                    return
                }
            }
            app.signedInUser.favouriteHillforts += id
            app.users.updateUser(app.signedInUser)
        }
    }

    fun removeFavourite(){
        if(selectedHillFort != null){
            val id = selectedHillFort!!.id
            var current = 0
            app.signedInUser.favouriteHillforts.forEach {
                if(it == id) {
                    app.signedInUser.favouriteHillforts = app.signedInUser.favouriteHillforts.filterIndexed { index, _ -> index != current }
                    app.users.updateUser(app.signedInUser)
                    return
                }
                current += 1
            }
        }
    }

    fun openSearch() {
        val window = PopupWindow(view!!.cardView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
        val popUp = view!!.layoutInflater.inflate(R.layout.search_popup, null)
        window.contentView = popUp
        window.showAtLocation(view!!.cardView, 10, 150, 50)

        // view!!.openSearch.isClickable = false

        val titleLabel = popUp.findViewById<TextView>(R.id.searchTitle)
        val ratingMaxLabel = popUp.findViewById<TextView>(R.id.searchRatingMax)
        val ratingMinLabel = popUp.findViewById<TextView>(R.id.searchRatingMin)
        val latMaxLabel = popUp.findViewById<TextView>(R.id.searchLatMax)
        val latMinLabel = popUp.findViewById<TextView>(R.id.searchLatMin)
        val lngMaxLabel = popUp.findViewById<TextView>(R.id.searchLngMax)
        val lngMinLabel = popUp.findViewById<TextView>(R.id.searchLngMin)
        val searchButton = popUp.findViewById<Button>(R.id.searchSubmit)
        val cancelButton = popUp.findViewById<Button>(R.id.searchCancel)
        val searchSwitch = popUp.findViewById<Switch>(R.id.searchSwitch)
        var switchOption = false
        searchButton.setOnClickListener {

            val title = titleLabel.text
            var ratingMax = ratingMaxLabel.text
            var ratingMin = ratingMinLabel.text
            var latMax = latMaxLabel.text
            var latMin = latMinLabel.text
            var lngMax = lngMaxLabel.text
            var lngMin = lngMinLabel.text
            var errors = false

            if(ratingMax.isEmpty()) {
                ratingMax = "-1.0"
            } else if(ratingMax.toString().toDouble() < 0 || ratingMax.toString().toDouble() > 5){
                view!!.toast("Invalid Rating")
                errors = true
            }

            if(ratingMin.isEmpty()) {
                ratingMin = "-1.0"
            } else if(ratingMin.toString().toDouble() < 0 || ratingMin.toString().toDouble() > 5){
                view!!.toast("Invalid Rating")
                errors = true
            }

            if(latMax.isEmpty()) {
                latMax = "-1.0"
            } else if(latMax.toString().toDouble() < -90 || latMax.toString().toDouble() > 90){
                view!!.toast("Invalid Lat Max")
                errors = true
            }
            if(latMin.isEmpty()) {
                latMin = "-1.0"
            } else if(latMin.toString().toDouble() < -90 || latMin.toString().toDouble() > 90){
                view!!.toast("Invalid Lat Min")
                errors = true
            }

            if(lngMax.isEmpty()) {
                lngMax = "-1.0"
            } else if(lngMax.toString().toDouble() < -180 || lngMax.toString().toDouble() > 180){
                view!!.toast("Invalid Long Max")
                errors = true
            }

            if(lngMin.isEmpty()) {
                lngMin = "-1.0"
            } else if(lngMin.toString().toDouble() < -180 || lngMin.toString().toDouble() > 180){
                view!!.toast("Invalid Long Min")
                errors = true
            }

            if(!errors) {
                val hillForts: List<HillFortModel> = if (!switchOption) {
                    app.users.getAllPublicHillforts()
                } else {
                    app.users.getAllFavourites(app.signedInUser)
                }

                publicHillforts = app.users.filterList(
                    hillForts,
                    title.toString(),
                    ratingMax.toString().toDouble(),
                    ratingMin.toString().toDouble(),
                    latMax.toString().toDouble(),
                    latMin.toString().toDouble(),
                    lngMax.toString().toDouble(),
                    lngMin.toString().toDouble()
                )

                doConfigMap(map, listener, "2")
                window.dismiss()
                view!!.openSearch.isClickable = true
            }

        }
        cancelButton.setOnClickListener {
            window.dismiss()
            view!!.openSearch.isClickable = true
        }

        searchSwitch.setOnCheckedChangeListener { _, isChecked ->
            if(!isChecked){
                searchSwitch.setText("All Hillforts")
                switchOption = false
            } else {
                searchSwitch.setText("Favourites")
                switchOption = true
            }
        }
    }

    fun doCopy() {
        if (selectedHillFort != null) {
            val user = app.users.findUserByHillfort(selectedHillFort!!)
            if(user == app.signedInUser){
                view!!.toast("That is your HillFort!")
            } else {
                val isOwned = app.users.alreadyOwned(app.signedInUser, selectedHillFort!!)
                if (!isOwned) {
                    val copiedHillfort = selectedHillFort!!.copy()
                    copiedHillfort.owned = selectedHillFort!!.id.toString()
                    copiedHillfort.public = false
                    app.users.createHillFort(app.signedInUser, copiedHillfort)
                } else {
                    view!!.toast("Already Copied HillFort!")
                }
            }
        }
    }
}
