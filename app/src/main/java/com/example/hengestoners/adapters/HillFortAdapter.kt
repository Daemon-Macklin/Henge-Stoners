package com.example.hengestoners.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.example.hengestoners.R
import com.example.hengestoners.activities.HillFortActivity
import com.example.hengestoners.helpers.readImage
import com.example.hengestoners.helpers.readImageFromPath
import com.example.hengestoners.models.HillFortModel
import kotlinx.android.synthetic.main.activity_hengestoners.view.*
import kotlinx.android.synthetic.main.card_hillfort.view.*

interface HillFortListener {
    fun onHillFortClick(hillFort: HillFortModel)
}

class HillFortAdapter constructor(
    private var hillForts: List<HillFortModel>,
    private val listener: HillFortListener
) :
    RecyclerView.Adapter<HillFortAdapter.MainHolder>() {

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

    override fun getItemCount(): Int = hillForts.size

    class MainHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(hillFort: HillFortModel, listener: HillFortListener) {
            itemView.hillFortTitle.text = hillFort.title
            itemView.description.text = hillFort.description
            itemView.setOnClickListener { listener.onHillFortClick(hillFort)}
            // val viewPager = itemView.findViewById<ViewPager>(R.id.listViewPager)
            var adapter = ImagePagerAdapter(hillFort.images, itemView.context)
            itemView.listViewPager.adapter = adapter
        }
    }
}