package com.example.hengestoners.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hengestoners.R
import com.example.hengestoners.adapters.HillFortAdapter
import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.adapters.NoteAdapter
import com.example.hengestoners.helpers.readImage
import com.example.hengestoners.helpers.readImageFromPath
import com.example.hengestoners.helpers.showImagePicker
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hengestoners.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HillFortActivity : AppCompatActivity(), AnkoLogger {

    lateinit var app : MainApp
    var hillFort = HillFortModel()
    val IMAGE_REQUEST = 1

    override fun onCreate(savedInstanceState: Bundle?) {

        var edit = false


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hengestoners)

        hillFortDateField.visibility = View.INVISIBLE
        removeImage.visibility = View.INVISIBLE

        app = application as MainApp

        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        val layoutManager = LinearLayoutManager(this)
        notesRecyclerView.layoutManager = layoutManager

        if(intent.hasExtra("hillFort_edit")){
            edit = true
            hillFortAdd.setText("Update HillFort")
            chooseImage.setText("Update Image")
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


            hillFortVisited.isChecked = hillFort.visited

            if(hillFort.visited){
                hillFortDateField.visibility = View.VISIBLE
                hillFortDateField.setText(hillFort.dateVisited.toString())
            }

            if(hillFort.dateVisited != LocalDate.MIN)
            hillFortDateField.setText(hillFort.dateVisited.toString())

            var adapter = ImagePagerAdapter(hillFort.images, this)
            hillFortImage.adapter = adapter

            if(hillFort.images.isNotEmpty()){
                removeImage.visibility = View.VISIBLE
            }
        }

        notesRecyclerView.adapter = NoteAdapter(hillFort.notes)

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

                var date = LocalDate.MIN

                try {
                    date = LocalDate.parse(hillFortDateField.text.toString(), DateTimeFormatter.ISO_DATE)
                } catch (e: Exception) {
                    info(e)
                }

                hillFort.dateVisited = date

                info("Adding $hillFort")

                if(edit){
                    app.hillForts.update(hillFort)
                }else {
                    app.hillForts.create(hillFort)
                }
                app.hillForts.logAll()
                setResult(AppCompatActivity.RESULT_OK)
                hillFort = HillFortModel()
                finish()
            }else {
                toast(R.string.title_warning)
            }
        }

        hillFortVisited.setOnClickListener{
            hillFort.visited = hillFortVisited.isChecked.toString().toBoolean()
            when (hillFort.visited != null) {
                hillFort.visited -> hillFortDateField.visibility = View.VISIBLE
                !hillFort.visited -> hillFortDateField.visibility = View.INVISIBLE
            }
        }

        hillFortAddNote.setOnClickListener() {
            if (hillFortNote.text.toString().isNotEmpty()) {
                hillFort.notes += hillFortNote.text.toString()
                hillFortNote.setText("")
                notesRecyclerView.adapter = NoteAdapter(hillFort.notes)
            }
            else
                toast(R.string.note_warning)
        }

        chooseImage.setOnClickListener() {
            showImagePicker(this, IMAGE_REQUEST)
        }

        removeImage.setOnClickListener() {
            if (hillFort.images.isEmpty()) {
                toast("No Images to Remove")
            } else {
                info(hillFortImage.currentItem.toString())
                hillFort.images = hillFort.images.filterIndexed { index, s -> index != hillFortImage.currentItem }
                var adapter = ImagePagerAdapter(hillFort.images, this)
                hillFortImage.adapter = adapter
                if (hillFort.images.isEmpty()){
                    removeImage.visibility = View.INVISIBLE
                }
            }
        }

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            IMAGE_REQUEST -> {
                if (data != null) {
                    hillFort.images += data.getData().toString()
                    var adapter = ImagePagerAdapter(hillFort.images, this)
                    hillFortImage.adapter = adapter
                    removeImage.visibility = View.VISIBLE
                }
            }
        }
    }
}
