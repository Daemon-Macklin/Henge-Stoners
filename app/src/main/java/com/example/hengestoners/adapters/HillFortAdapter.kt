package com.example.hengestoners.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hengestoners.R
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.card_hillfort.view.*
// Adapter to show hillfort data in the list activity recylerView

// Interfact to handle when an item is clicked
interface HillFortListener {
    fun onHillFortClick(hillFort: HillFortModel)
}


class HillFortAdapter constructor(
    private var hillForts: List<HillFortModel>,
    private val listener: HillFortListener
) :
    RecyclerView.Adapter<HillFortAdapter.MainHolder>() {

    // Function to set the custom card_hillfort.xml as the layout
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        return MainHolder(
            LayoutInflater.from(parent?.context).inflate(
                R.layout.card_hillfort,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val hillForts = hillForts[holder.adapterPosition]
        holder.bind(hillForts, listener)
    }

    // Get item count
    override fun getItemCount(): Int = hillForts.size

    // Constructor for class given
    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        // Function to set data into the view items
        fun bind(hillFort: HillFortModel, listener: HillFortListener) {

            // Set data
            itemView.hillFortTitle.text = hillFort.title
            itemView.description.text = hillFort.description
            itemView.setOnClickListener { listener.onHillFortClick(hillFort)}
            var adapter = ImagePagerAdapter(hillFort.images, itemView.context)
            itemView.listViewPager.adapter = adapter
            var str = "Lat and Long not set"
            if(hillFort.location["lat"]!! <= 90) {
                str = "lat = " + hillFort.location["lat"].toString() + "\nLong = " + hillFort.location["long"].toString()
            }
            itemView.latlng.text = str
            itemView.cardVisitedCheckBox.isChecked = hillFort.visited
            itemView.isPublic.isChecked = hillFort.public
        }
    }
}