package com.example.hengestoners.views.Navigation

import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW

class NavigationPresenter(view: BaseView): BasePresenter(view) {


    fun toHome() {
        view?.navigateTo(VIEW.LIST)

    }

    fun toSettings(){
        view?.navigateTo(VIEW.SETTINGS)
    }

    fun toMapView() {
        view?.navigateTo(VIEW.MAPS)
    }

    fun toLogOut() {
        view?.navigateTo(VIEW.LOGIN)
        view?.finish()
    }
}
