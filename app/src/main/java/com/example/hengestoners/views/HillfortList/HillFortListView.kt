package com.example.hengestoners.views.HillfortList

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hengestoners.R
import com.example.hengestoners.views.MapView.MapViewView
import com.example.hengestoners.views.Login.LoginView
import com.example.hengestoners.views.Settings.SettingsView
import com.example.hengestoners.adapters.HillFortListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Navigation.NavigationPresenter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor

// Hillfortlistactivity - Activity to list all users hillforts
class HillFortListView : BaseView(), HillFortListener {

    lateinit var app: MainApp
    lateinit var presenter: HillFortListPresenter
    lateinit var nagivation: NavigationPresenter

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
        presenter = initPresenter(HillFortListPresenter(this)) as HillFortListPresenter
        nagivation = initPresenter(NavigationPresenter(this)) as NavigationPresenter
        app = application as MainApp

        // Set the title of the toolbar and the navbar
        toolbar.title = "$title - ${app.signedInUser.userName}"
        navToolBar.title = app.signedInUser.userName
        setSupportActionBar(toolbar)

        // Make the navbar invisible by default
        navigationView.visibility = View.INVISIBLE

        // Create a new layout manager for the recylerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        // Load all the users hillforts
        presenter.doLoadUserData(this)

        // Function to handle when navbar toggle button is pressed
        navToggleButton.setOnClickListener() {
            nagivation.showNav(navigationView)
        }

        // Set the nav home button to be false as we are already here
        HomeButton.isEnabled = false

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

        MapsActivityButton.setOnClickListener {
            nagivation.toMapView()
        }
    }

    // Function to set tool bar to be custom layout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Item to handle add hillfort button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_add -> presenter.doAddPlacemark()
        }

        return super.onOptionsItemSelected(item)
    }

    // Function to handle when hillfort recyler view item is pressed
    override fun onHillFortClick(hillFort: HillFortModel) {
        presenter.doEditHillFort(hillFort)
    }

    // When back is pressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // load all the hillforts
        presenter.doLoadUserData(this)
        super.onActivityResult(requestCode, resultCode, data)
    }

}



