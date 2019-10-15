package com.example.hengestoners.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import com.example.hengestoners.R
import com.example.hengestoners.helpers.readImageFromPath
import kotlinx.android.synthetic.main.card_hillfort.view.*
import kotlinx.android.synthetic.main.card_notes.view.*

class ImagePagerAdapter(
    private val images: List<String>,
    private val context: Context
):

    PagerAdapter() {
    override fun isViewFromObject(view: View, image: Any): Boolean {
        return view === image
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.setImageBitmap(readImageFromPath(context, images[position]))
        return imageView
    }

    override fun getCount(): Int = images.size

    override fun destroyItem(container: ViewGroup, position: Int, image: Any) {
        container.removeView(image as ImageView)
    }

}