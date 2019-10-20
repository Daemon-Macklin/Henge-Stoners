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
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class ImagePagerAdapter (
    private val images: List<String>,
    private val context: Context
):

    PagerAdapter(), AnkoLogger {

    override fun isViewFromObject(view: View, image: Any): Boolean {
        return view === image
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageView = ImageView(context)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.setImageBitmap(readImageFromPath(context, images[position]))
        view.addView(imageView,0)
        return imageView
    }

    override fun getCount(): Int = images.size

    override fun destroyItem(container: ViewGroup, position: Int, image: Any) {
        container.removeView(image as ImageView)
    }

}