package com.example.hengestoners.presenters

import com.example.hengestoners.activities.HillFortListActivity
import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hillfort_list.*

class HillFortListPresenter(val view: HillFortListActivity) {

    lateinit var app: MainApp

    init {
        app = view.application as MainApp
    }


    fun doLoadUserData(){
        // Show all of the signed in users hillforts
        doShowHillForts(app.signedInUser.hillForts)
    }

    fun doShowHillForts(hillForts: List<HillFortModel>){
        // Reset the recylerview with a new adapter with the given list
        view.recyclerView.adapter = HillFortAdapter(hillForts, view)
        view.recyclerView.adapter?.notifyDataSetChanged()
    }
}