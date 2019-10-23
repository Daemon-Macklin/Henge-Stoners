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
import com.example.hengestoners.adapters.NotesListener
import com.example.hengestoners.helpers.readImage
import com.example.hengestoners.helpers.readImageFromPath
import com.example.hengestoners.helpers.showImagePicker
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hengestoners.*
import kotlinx.android.synthetic.main.activity_hillfort_list.*
import kotlinx.android.synthetic.main.card_hillfort.view.*
import org.jetbrains.anko.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class HillFortActivity : AppCompatActivity(), NotesListener, AnkoLogger {

    lateinit var app : MainApp
    var hillFort = HillFortModel()
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    override fun onCreate(savedInstanceState: Bundle?) {

        var edit = false


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hengestoners)

        hillFortDateField.visibility = View.INVISIBLE
        removeImage.visibility = View.INVISIBLE
        hillFortRemove.visibility = View.INVISIBLE

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

            hillFortVisited.isChecked = hillFort.visited

            if(hillFort.visited){
                hillFortDateField.visibility = View.VISIBLE
                hillFortDateField.setText(hillFort.dateVisited.toString())
            }

            if(hillFort.dateVisited != "")
            hillFortDateField.setText(hillFort.dateVisited)

            var adapter = ImagePagerAdapter(hillFort.images, this)
            hillFortImage.adapter = adapter

            if(hillFort.images.isNotEmpty()){
                removeImage.visibility = View.VISIBLE
            }

            hillFortRemove.visibility = View.VISIBLE

            var str = "Lat and Long not set"
            if(hillFort.location["lat"]!! <= 90) {
                str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
            }
            hillFortLocationDisplay.text = str
        }

        notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)

        hillFortAdd.setOnClickListener() {

            if(hillFortTitleField.text.toString().isNotEmpty()){

                hillFort.title = hillFortTitleField.text.toString()
                hillFort.description = hillFortDescriptionField.text.toString()

                var validDate = false

                try {
                    LocalDate.parse(hillFortDateField.text.toString(), DateTimeFormatter.ISO_DATE)
                    validDate = true
                } catch (e: Exception) {
                    info(e)
                }

                if(validDate) {
                    hillFort.dateVisited = hillFortDateField.text.toString()
                }

                info("Adding $hillFort")

                if(edit){
                    app.signedInUser.
                    app.hillForts.update(hillFort)
                }else {
                    app.signedInUser.hillForts.create(hillFort)
                }
                app.signedInUser.hillForts.logAll()
                setResult(AppCompatActivity.RESULT_OK)
                hillFort = HillFortModel()
                finish()
            }else {
                toast(R.string.title_warning)
            }
        }

        hillFortRemove.setOnClickListener() {
            app.signedInUser.hillForts.remove(hillFort)
            finish()
        }

        hillFortLocation.setOnClickListener() {
            startActivityForResult(intentFor<MapActivity>().putExtra("hillFort", hillFort), LOCATION_REQUEST)
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
                notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)
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
                hillFort.images = hillFort.images.filterIndexed { index, _ -> index != hillFortImage.currentItem }
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
                    hillFort.images += data.data.toString()
                    var adapter = ImagePagerAdapter(hillFort.images, this)
                    hillFortImage.adapter = adapter
                    removeImage.visibility = View.VISIBLE
                }
            }
            LOCATION_REQUEST -> {
                if (data != null) {
                    hillFort = data.extras?.getParcelable("hillFort")!!
                    info(hillFort.location)
                    val str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
                    hillFortLocationDisplay.text = str
                }
            }
        }
    }

    override fun onNoteClicked(removeIndex: Int){
        hillFort.notes = hillFort.notes.filterIndexed { index, s -> index != removeIndex }
        notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)
    }
}
