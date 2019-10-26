package com.example.hengestoners.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_notes.view.*

// Interface to handle when note is clicked
interface NotesListener {
    fun onNoteClicked(removeIndex: Int)
}

// Class for the notes data on the hillfort activity
class NoteAdapter constructor(
    private var notes: List<String>,
    private val listener: NotesListener

) :
    RecyclerView.Adapter<NoteAdapter.MainHolder>() {

    // When it is created
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        // Create a new holder with the card_notes.xml as the layout
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_notes,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val notes = notes[holder.adapterPosition]
        holder.bind(notes, position, listener)
    }

    override fun getItemCount(): Int = notes.size

    // Holder for data
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: String, position: Int, listener: NotesListener) {
            // Set the data
            itemView.noteString.text = note
            // Create an onclick listener that will call the onNoteClicked method in the list activity
            itemView.setOnClickListener{ listener.onNoteClicked(position) }
        }
    }


}