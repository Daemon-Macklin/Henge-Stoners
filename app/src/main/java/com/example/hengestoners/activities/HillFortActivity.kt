package com.example.hengestoners.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import com.example.hengestoners.R
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hengestoners.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HillFortActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp

    override fun onCreate(savedInstanceState: Bundle?) {

        var hillFort = HillFortModel()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hengestoners)

        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        if(intent.hasExtra("hillFort_edit")){

            hillFort = intent.extras?.getParcelable<HillFortModel>("hillFort_edit")!!
            hillFortTitleField.setText(hillFort.title)
            hillFortDescriptionField.setText(hillFort.description)
            var lat = hillFort.location["lat"]
            var long = hillFort.location["long"]

            if (lat != null && long != null) {
                when {
                    lat > 90 -> hillFortLatField.setText("")
                    else -> hillFortLatField.setText(lat.toString())
                }
                when {
                    long > 180 -> hillFortLongField.setText("")
                    else -> hillFortLongField.setText(long.toString())
                }
            }

            if(hillFort.visited)
                hillFortVisited.setChecked(true)

            if(hillFort.dateVisited != LocalDate.MIN)
            hillFortDateField.setText(hillFort.dateVisited.toString())

        }

        hillFortAdd.setOnClickListener() {

            if(hillFortTitleField.text.toString().isNotEmpty()){

                hillFort.title = hillFortTitleField.text.toString()
                hillFort.description = hillFortDescriptionField.text.toString()


                val lat = hillFortLatField.text.toString()
                val long = hillFortLongField.text.toString()

                if(lat.isNotBlank())
                    hillFort.location["lat"] = lat.toDouble()

                if(long.isNotBlank())
                    hillFort.location["long"] = long.toDouble()

                hillFort.visited = hillFortVisited.text.toString().toBoolean()

                var date = LocalDate.MIN

                try {
                    date = LocalDate.parse(hillFortDateField.text.toString(), DateTimeFormatter.ISO_DATE)
                } catch (e: Exception) {
                    info(e)
                }

                hillFort.dateVisited = date

                info("Adding $hillFort")
                app.hillForts.create(hillFort)
                app.hillForts.logAll()
                setResult(AppCompatActivity.RESULT_OK)
                finish()
            }else {
                toast("Please enter title")
            }
        }

        hillFortAddNote.setOnClickListener() {
            toast("Add Note")
        }
    }

    fun test() {
        info("test")
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_placemark, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
