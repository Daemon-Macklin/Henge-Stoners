package com.example.hengestoners.views.Navigation

import android.view.View
import androidx.core.view.isVisible
import com.example.hengestoners.models.UserModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import com.example.hengestoners.views.Base.VIEW
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_hillfort_list.*

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
        app.signedInUser = UserModel()
        view?.navigateTo(VIEW.LOGIN)
        view?.finish()
    }
}
