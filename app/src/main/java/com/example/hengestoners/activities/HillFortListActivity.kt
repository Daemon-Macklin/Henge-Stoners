package com.example.hengestoners.activities

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hengestoners.R
import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.adapters.HillFortListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.models.UserModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.startActivityForResult
import org.jetbrains.anko.toast

// Hillfortlistactivity - Activity to list all users hillforts
class HillFortListActivity : AppCompatActivity(), HillFortListener {

    lateinit var app: MainApp

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hillfort_list)
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
        loadHillForts()

        // Function to handle when navbar toggle button is pressed
        navToggleButton.setOnClickListener() {

            // Show or hide the nav bar depending on it's current state
            when(navigationView != null){
                navigationView.isVisible -> navigationView.visibility = View.INVISIBLE
                !navigationView.isVisible -> navigationView.visibility = View.VISIBLE
            }
        }

        // Set the nav home button to be false as we are already here
        HomeButton.isEnabled = false

        // Function to handle pressing the settings button
        SettingsButton.setOnClickListener() {

            // Start the settings activity
            startActivity(intentFor<SettingsActivity>())
        }

        // Function to handle pressing the logout button
        LogOutButton.setOnClickListener() {

            // Log the user out by resetting the signed in user, and starting the login activity
            app.signedInUser = UserModel()
            startActivity(intentFor<LoginActivity>())

            // If back is pressed finish
            finish()
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
            R.id.item_add -> startActivityForResult<HillFortActivity>(0)
        }

        return super.onOptionsItemSelected(item)
    }

    // Function to handle when hillfort recyler view item is pressed
    override fun onHillFortClick(hillFort: HillFortModel) {

        // Start the hillfort activity and add the edit flag
        startActivityForResult(intentFor<HillFortActivity>().putExtra("hillFort_edit", hillFort), 0)
    }

    // When back is pressed
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        // load all the hillforts
        loadHillForts()
        super.onActivityResult(requestCode, resultCode, data)
    }

    // Private function to load all of the users hillforts
    private fun loadHillForts() {

        // Show all of the signed in users hillforts
        showHillForts(app.signedInUser.hillForts)
    }

    // Private function to show all hillforts in the given list
    private fun showHillForts(hillForts: List<HillFortModel>){

        // Reset the recylerview with a new adapter with the given list
        recyclerView.adapter = HillFortAdapter(hillForts, this)
        recyclerView.adapter?.notifyDataSetChanged()
    }

}



