package com.example.hengestoners.activities

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
import com.example.hengestoners.helpers.showImagePicker
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hengestoners.*
import org.jetbrains.anko.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter

// Hillfort Activity - Activity for creating/editing and view hillforts
class HillFortActivity : AppCompatActivity(), NotesListener, AnkoLogger {

    lateinit var app : MainApp
    var hillFort = HillFortModel()
    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    /**
     * On Create Method run at the start of activity
     */
    override fun onCreate(savedInstanceState: Bundle?) {

        var edit = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hengestoners)

        // Set views to invisible
        hillFortDateField.visibility = View.INVISIBLE
        removeImage.visibility = View.INVISIBLE
        hillFortRemove.visibility = View.INVISIBLE

        app = application as MainApp

        // Add the title to the tool bar and add the tool bar
        toolbarAdd.title = title
        setSupportActionBar(toolbarAdd)

        // Create new layout manager for the notes view
        val layoutManager = LinearLayoutManager(this)
        notesRecyclerView.layoutManager = layoutManager

        // Check to see if the user is editing a hillfort
        if(intent.hasExtra("hillFort_edit")){

            // If he is we add data to the fields
            edit = true
            hillFortAdd.text = "Update HillFort"
            chooseImage.text = "Update Image"
            hillFort = intent.extras?.getParcelable<HillFortModel>("hillFort_edit")!!
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

        // Set the notes recyler view adapter to be my NoteAdapter containing all of the notes
        notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)

        // Function when add button is pressed
        hillFortAdd.setOnClickListener() {

            // If there is data in the title field
            if(hillFortTitleField.text.toString().isNotEmpty()){

                // Gather data form the fields
                hillFort.title = hillFortTitleField.text.toString()
                hillFort.description = hillFortDescriptionField.text.toString()

                // Check if the date is valid by casting it as a local date
                var validDate = false
                try {
                    LocalDate.parse(hillFortDateField.text.toString(), DateTimeFormatter.ISO_DATE)
                    validDate = true
                } catch (e: Exception) {
                    info(e)
                }

                // If the date is valid of if this hillfort has not been visited
                if(validDate || !hillFort.visited) {

                    // We can add the hillfort
                    hillFort.dateVisited = hillFortDateField.text.toString()
                    info("Adding $hillFort")

                    // Check if we are editing the hillfort or not to see which usersJSONStore method to call
                    if(edit){

                        // If we are we call the updateHillFort function
                        app.users.updateHillFort(app.signedInUser, hillFort)
                    }else {

                        // Else this is a new hillfort so we call the createHillFort function
                        app.users.createHillFort(app.signedInUser, hillFort)
                    }

                    // After we add the hill fort log it all, reset the hillfort and return to the list activity with a RESUlT_OK
                    app.users.logAllHillForts(app.signedInUser)
                    setResult(RESULT_OK)
                    hillFort = HillFortModel()
                    finish()
                } else {
                    // If the date is not valid inform the user
                    toast("Input Valid Date YYYY-MM-DD")
                }
            }else {
                // If there is not title tell the user
                toast(R.string.title_warning)
            }
        }

        // Function when remove hillfort button is pressed
        hillFortRemove.setOnClickListener() {
            // Call the remove hillfort function in the user store and finish
            app.users.removeHillFort(app.signedInUser, hillFort)
            finish()
        }

        // Function for set location button
        hillFortLocation.setOnClickListener() {
            // Start the maps activity, sending the hillfort object and wait for the location_request result
            startActivityForResult(intentFor<MapActivity>().putExtra("hillFort", hillFort), LOCATION_REQUEST)
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

                // Add the string to the notes arraylist, reset the text field and reset the adapter with the new list
                hillFort.notes += hillFortNote.text.toString()
                hillFortNote.setText("")
                notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)
            }
            else
                toast(R.string.note_warning)
        }

        // Function for choose image button
        chooseImage.setOnClickListener() {

            // Show the image picker and wait for the image_request result
            showImagePicker(this, IMAGE_REQUEST)
        }

        // Function for remove image button
        removeImage.setOnClickListener() {

            // If there are no images we can't remove anything
            if (hillFort.images.isEmpty()) {
                toast("No Images to Remove")
            } else {

                // If images are present
                info(hillFortImage.currentItem.toString())

                // Filter the image list so that the image currently displayed in the view pager is removed
                hillFort.images = hillFort.images.filterIndexed { index, _ -> index != hillFortImage.currentItem }

                // Reset the adapter
                var adapter = ImagePagerAdapter(hillFort.images, this)
                hillFortImage.adapter = adapter

                // If the list is now empty hide the delete button
                if (hillFort.images.isEmpty()){
                    removeImage.visibility = View.INVISIBLE
                }
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

        // When there is a request code
        when (requestCode) {

            // If it is image_request i.e 1 and the data is not null
            IMAGE_REQUEST -> {
                if (data != null) {

                    // Add the new image to the images list
                    hillFort.images += data.data.toString()

                    // Update the adapter and make the delete button visible
                    var adapter = ImagePagerAdapter(hillFort.images, this)
                    hillFortImage.adapter = adapter
                    removeImage.visibility = View.VISIBLE
                }
            }

            // If it is location_request i.e 2 and the data is not null
            LOCATION_REQUEST -> {
                if (data != null) {

                    // Make the hillfort object equal the one returned which contains the location data
                    hillFort = data.extras?.getParcelable("hillFort")!!
                    info(hillFort.location)

                    // Display the new location data
                    val str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
                    hillFortLocationDisplay.text = str
                }
            }
        }
    }

    // Function for when a note item in the recylerview is pressed
    override fun onNoteClicked(removeIndex: Int){
        // When pressed remove the item from the list and reset the adapter with the new list
        hillFort.notes = hillFort.notes.filterIndexed { index, s -> index != removeIndex }
        notesRecyclerView.adapter = NoteAdapter(hillFort.notes, this)
    }
}
