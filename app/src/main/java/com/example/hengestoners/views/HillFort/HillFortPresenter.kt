package com.example.hengestoners.views.HillFort

import android.content.Intent
import android.view.View
import com.example.hengestoners.views.Location.EditLocationView
import com.example.hengestoners.adapters.ImagePagerAdapter
import com.example.hengestoners.adapters.NoteAdapter
import com.example.hengestoners.adapters.NotesListener
import com.example.hengestoners.helpers.showImagePicker
import com.example.hengestoners.main.MainApp
import com.example.hengestoners.models.HillFortModel
import com.example.hengestoners.views.Base.BasePresenter
import com.example.hengestoners.views.Base.BaseView
import kotlinx.android.synthetic.main.activity_hengestoners.*
import org.jetbrains.anko.info
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.toast
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class HillFortPresenter(view: BaseView): BasePresenter(view) {

    val IMAGE_REQUEST = 1
    val LOCATION_REQUEST = 2

    var hillFort = HillFortModel()
    var edit = false

    init {
        app = view.application as MainApp
        if (view.intent.hasExtra("hillFort_edit")) {
            edit = true
            hillFort = view.intent.extras?.getParcelable<HillFortModel>("hillFort_edit")!!
            view.showHillfort(hillFort)
        }
    }

    fun doSave(title: String, description: String, dateVisited: String){

        hillFort.title = title
        hillFort.description = description

            // Check if the date is valid by casting it as a local date
        var validDate = false
        try {
            LocalDate.parse(dateVisited, DateTimeFormatter.ISO_DATE)
            validDate = true
        } catch (e: Exception) {
            // info(e)
        }

        // If the date is valid or if this hillfort has not been visited
        if(validDate || !hillFort.visited) {

            // We can add the hillfort
            hillFort.dateVisited = dateVisited

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
            hillFort = HillFortModel()
            view?.finish()
        } else {
            // If the date is not valid inform the user
            view?.toast("Input Valid Date YYYY-MM-DD")
        }
    }


    fun doAddNote(note: String, listener: NotesListener) {
        // Add the string to the notes arraylist, reset the text field and reset the adapter with the new list
        view.let {
            hillFort.notes += note
            view!!.hillFortNote.setText("")
            view!!.notesRecyclerView.adapter = NoteAdapter(hillFort.notes, listener)
        }
    }

    fun doImagePicker(){
        // Show the image picker and wait for the image_request result
        view.let{
        showImagePicker(view!!, IMAGE_REQUEST)
        }
    }

    fun doLocationPick() {
        // Start the maps activity, sending the hillfort object and wait for the location_request result
        view!!.let {
            view!!.startActivityForResult(
                view!!.intentFor<EditLocationView>().putExtra(
                    "hillFort",
                    hillFort
                ), LOCATION_REQUEST
            )
        }
    }

    override fun doActivityResult(requestCode: Int, resultCode: Int, data: Intent){
        // When there is a request code
        when (requestCode) {

            // If it is image_request i.e 1 and the data is not null
            IMAGE_REQUEST -> {
                if (data != null) {

                    // Add the new image to the images list
                    hillFort.images += data.data.toString()

                    // Update the adapter and make the delete button visible
                    var adapter = ImagePagerAdapter(hillFort.images, view!!)
                    view!!.hillFortImage.adapter = adapter
                    view!!.removeImage.visibility = View.VISIBLE
                }
            }

            // If it is location_request i.e 2 and the data is not null
            LOCATION_REQUEST -> {
                if (data != null) {

                    // Make the hillfort object equal the one returned which contains the location data
                    hillFort = data.extras?.getParcelable("hillFort")!!
                    view!!.info(hillFort.location)

                    // Display the new location data
                    val str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
                    view!!.hillFortLocationDisplay.text = str
                }
            }
        }
    }

    fun doRemoveNote(removeIndex: Int, listener: NotesListener){
        // When pressed remove the item from the list and reset the adapter with the new list
        hillFort.notes = hillFort.notes.filterIndexed { index, s -> index != removeIndex }
        view!!.notesRecyclerView.adapter = NoteAdapter(hillFort.notes, listener)
    }

    fun doRemoveHillfort(hillFortRemove: HillFortModel){
        // Call the remove hillfort function in the user store and finish
        app.users.removeHillFort(app.signedInUser, hillFortRemove)
        view!!.finish()
    }

    fun doRemoveImage(imageIndex: Int){
        // Filter the image list so that the image currently displayed in the view pager is removed
        hillFort.images = hillFort.images.filterIndexed { index, _ -> index != imageIndex }

        // Reset the adapter
        var adapter = ImagePagerAdapter(hillFort.images, view!!)
        view!!.hillFortImage.adapter = adapter

        // If the list is now empty hide the delete button
        if (hillFort.images.isEmpty()){
            view!!.removeImage.visibility = View.INVISIBLE
        }
    }
}
