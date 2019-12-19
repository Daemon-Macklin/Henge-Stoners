package com.example.hengestoners.views.Base

import android.content.Intent
import android.os.Parcelable
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.HillFort.HillFortView
import com.example.hengestoners.views.HillfortList.HillFortListView
import com.example.hengestoners.views.Location.EditLocationView
import com.example.hengestoners.views.Login.LoginView
import com.example.hengestoners.views.MapView.MapViewView
import com.example.hengestoners.views.Register.RegisterView
import com.example.hengestoners.views.Settings.SettingsView
import kotlinx.android.synthetic.main.content_map_view.*
import org.jetbrains.anko.AnkoLogger



val IMAGE_REQUEST = 1
val LOCATION_REQUEST = 2


enum class VIEW {
    LOCATION, HILLFORT, MAPS, LIST, SETTINGS, LOGIN, REGISTER
}

open abstract class BaseView(): AppCompatActivity(), AnkoLogger {

    var basePresenter: BasePresenter? = null

    fun navigateTo(view: VIEW, code: Int = 0, key: String = "", value: Parcelable? = null) {
        var intent = Intent(this, HillFortListView::class.java)
        when (view) {
            VIEW.LOCATION -> intent = Intent(this, EditLocationView::class.java)
            VIEW.HILLFORT -> intent = Intent(this, HillFortView::class.java)
            VIEW.MAPS -> intent = Intent(this, MapViewView::class.java)
            VIEW.LIST -> intent = Intent(this, HillFortListView::class.java)
            VIEW.SETTINGS -> intent = Intent(this, SettingsView::class.java)
            VIEW.LOGIN -> intent = Intent(this, LoginView::class.java)
            VIEW.REGISTER -> intent = Intent(this, RegisterView::class.java)
        }
        if (key != "") {
            intent.putExtra(key, value)
        }
        startActivityForResult(intent, code)
    }

    fun initPresenter(presenter: BasePresenter): BasePresenter {
        basePresenter = presenter
        return presenter
    }

    fun init(toolbar: Toolbar, upEnabled: Boolean) {
        toolbar.title = title
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(upEnabled)
        if(upEnabled){
            toolbar.setNavigationOnClickListener{
                finish()
            }
        }
    }

    override fun onDestroy() {
        basePresenter?.onDestroy()
        super.onDestroy()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            basePresenter?.doActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        basePresenter?.doRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    open fun showHillfort(hillFort: HillFortModel) {}
    open fun showHillforts(hillForts: List<HillFortModel>) {}
    open fun showProgress() {}
    open fun hideProgress() {}
}
