package com.example.hengestoners.views.HillfortList

import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.adapters.HillFortListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.HillFort.HillFortView
import com.example.hengestoners.views.basePresenter.BasePresenter
import com.example.hengestoners.views.basePresenter.BaseView
import com.example.hengestoners.views.basePresenter.VIEW
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import org.jetbrains.anko.intentFor

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
        view?.navigateTo(VIEW.PLACEMARK, 0, "hillFort_edit", hillFort)
    }

    fun doAddPlacemark() {
        view?.navigateTo(VIEW.PLACEMARK)
    }
}