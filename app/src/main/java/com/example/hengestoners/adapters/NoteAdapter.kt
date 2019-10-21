package com.example.hengestoners.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_notes.view.*

interface NotesListener {
    fun onNoteClicked(removeIndex: Int)
}

class NoteAdapter constructor(
    private var notes: List<String>,
    private val listener: NotesListener

) :
    RecyclerView.Adapter<NoteAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
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

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(note: String, position: Int, listener: NotesListener) {
            itemView.noteString.text = note
            itemView.setOnClickListener{ listener.onNoteClicked(position) }
        }
    }


}