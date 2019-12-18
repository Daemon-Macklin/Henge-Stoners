package com.example.hengestoners.views.HillFort

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.hengestoners.R
import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.adapters.NoteAdapter
import com.example.hengestoners.adapters.NotesListener
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.basePresenter.BasePresenter
import com.example.hengestoners.views.basePresenter.BaseView
import kotlinx.android.synthetic.main.activity_hengestoners.*
import org.jetbrains.anko.*

// Hillfort Activity - Activity for creating/editing and view hillforts
class HillFortView : BaseView(), NotesListener, AnkoLogger {

    lateinit var app : MainApp
    var hillFort = HillFortModel()
    lateinit var presenter: HillFortPresenter
    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hengestoners)

        // Set views to invisible
        hillFortDateField.visibility = View.INVISIBLE
        removeImage.visibility = View.INVISIBLE
        hillFortRemove.visibility = View.INVISIBLE

        app = application as MainApp
        presenter = initPresenter(HillFortPresenter(this)) as HillFortPresenter

        // Add the title to the tool bar and add the tool bar
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        // Create new layout manager for the notes view
        val layoutManager = LinearLayoutManager(this)
        notesRecyclerView.layoutManager = layoutManager

        // Set the notes recyler view adapter to be my NoteAdapter containing all of the notes
        notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)

        // Function when add button is pressed
        hillFortAdd.setOnClickListener() {

            // If there is data in the title field
            if(hillFortTitleField.text.toString().isNotEmpty()){

                // Gather data form the fields
                hillFort.title = hillFortTitleField.text.toString()
                hillFort.description = hillFortDescriptionField.text.toString()
                hillFort.dateVisited = hillFortDateField.text.toString()
                presenter.doSave(hillFort.title, hillFort.description, hillFort.dateVisited)
            }else {
                // If there is not title tell the user
                toast(R.string.title_warning)
            }
        }

        // Function when remove hillfort button is pressed
        hillFortRemove.setOnClickListener() {
            presenter.doRemoveHillfort(hillFort)
        }

        // Function for set location button
        hillFortLocation.setOnClickListener() {
            presenter.doLocationPick()
        }

        // Function for hillfort visited check box
        hillFortVisited.setOnClickListener{

            // Set the visited value to be the value of the checkbox
            hillFort.visited = hillFortVisited.isChecked.toString().toBoolean()

            // Show or hide the date field depending on the visited value
            when (hillFort.visited != null) {
                hillFort.visited -> hillFortDateField.visibility = View.VISIBLE
                !hillFort.visited -> hillFortDateField.visibility = View.INVISIBLE
            }
        }

        // Function for add note button
        hillFortAddNote.setOnClickListener() {

            // If there is data in the text field
            if (hillFortNote.text.toString().isNotEmpty()) {
                presenter.doAddNote(hillFortNote.text.toString(), this)
            }
            else
                toast(R.string.note_warning)
        }

        // Function for choose image button
        chooseImage.setOnClickListener() {
            presenter.doImagePicker()
        }

        // Function for remove image button
        removeImage.setOnClickListener() {
            // If there are no images we can't remove anything
            if (hillFort.images.isEmpty()) {
                toast("No Images to Remove")
            } else {

                // If images are present
                info(hillFortImage.currentItem.toString())
                presenter.doRemoveImage(hillFortImage.currentItem)
            }

        }
    }

    // Function to set tool bar to be custom layout
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_hillfort, menu)
        return super.onCreateOptionsMenu(menu)
    }

    // Item to handle cancel button
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.item_cancel -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    // Function to listen for activity results and perform actions
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null) {
            presenter.doActivityResult(requestCode, resultCode, data)
        }
    }

    // Function for when a note item in the recylerview is pressed
    override fun onNoteClicked(removeIndex: Int){
        presenter.doRemoveNote(removeIndex, this)
    }

    override fun showHillfort(hillFortEdit: HillFortModel){
        hillFort = hillFortEdit
        // If he is we add data to the fields
        hillFortAdd.text = "Update HillFort"
        chooseImage.text = "Update Image"
        hillFortTitleField.setText(hillFort.title)
        hillFortDescriptionField.setText(hillFort.description)
        hillFortVisited.isChecked = hillFort.visited

        // Check if the hillfort is visited
        if(hillFort.visited){
            // If it is we can set show the date field and set it if there is data in the field
            hillFortDateField.visibility = View.VISIBLE
            if(hillFort.dateVisited != "")
                hillFortDateField.setText(hillFort.dateVisited)
        }

        // Set the view pager adapter to be my ImagePagerAdapter containing the hillfort images
        val adapter = ImagePagerAdapter(hillFort.images, this)
        hillFortImage.adapter = adapter

        // If there are images show the remove image button
        if(hillFort.images.isNotEmpty()){
            removeImage.visibility = View.VISIBLE
        }

        // Since we are editing the hillfort is exists so we can remove it
        hillFortRemove.visibility = View.VISIBLE

        // If the lat long is not the default show it, else show this string
        var str = "Lat and Long not set"
        if(hillFort.location["lat"]!! <= 90) {
            str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
        }
        hillFortLocationDisplay.text = str
    }
}
