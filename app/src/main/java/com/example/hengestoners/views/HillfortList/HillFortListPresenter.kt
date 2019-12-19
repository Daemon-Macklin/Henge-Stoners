package com.example.hengestoners.views.HillfortList

import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.adapters.HillFortListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import kotlinx.android.synthetic.main.activity_hillfort_list.*

class HillFortListPresenter(view: BaseView): BasePresenter(view) {

    init {
        app = view.application as MainApp
    }

    fun doLoadUserData(listener: HillFortListener){
        // Show all of the signed in users hillforts
        doShowHillForts(app.signedInUser.hillForts, listener)
    }

    fun doShowHillForts(hillForts: List<HillFortModel>, listener: HillFortListener){
        // Reset the recylerview with a new adapter with the given list
        view!!.recyclerView.adapter = HillFortAdapter(hillForts, listener)
        view!!.recyclerView.adapter?.notifyDataSetChanged()
    }

    fun doEditHillFort(hillFort: HillFortModel){
        // Start the hillfort activity and add the edit flag
        view?.navigateTo(VIEW.HILLFORT, 0, "hillFort_edit", hillFort)
    }

    fun doAddHillFort() {
        view?.navigateTo(VIEW.HILLFORT)
    }
}