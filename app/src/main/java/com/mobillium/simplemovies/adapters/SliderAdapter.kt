package com.mobillium.simplemovies.adapters

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.navigation.NavController
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.mobillium.simplemovies.R
import com.mobillium.simplemovies.response.playing.PlayingResult

class SliderAdapter(
    private var act: Activity?,
    private var navController: NavController? = null,
    private var items: List<PlayingResult>?
) : PagerAdapter() {

    private var onItemClickListener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onItemClick(view: View?, obj: PlayingResult?)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener?) {
        this.onItemClickListener = onItemClickListener
    }


    override fun getCount(): Int {
        return items!!.size
    }

    fun getItem(pos: Int): PlayingResult? {
        return items!![pos]
    }

    fun setItems(items: List<PlayingResult?>?) {
        this.items = items as List<PlayingResult>?
        notifyDataSetChanged()
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val o: PlayingResult = items!![position]
        val inflater = act!!.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val v: View = inflater.inflate(R.layout.item_slider_image, container, false)
        val image = v.findViewById<View>(R.id.image) as ImageView
        val title = v.findViewById<View>(R.id.slider_title) as TextView
        val desc = v.findViewById<View>(R.id.slider_desc) as TextView
        val layout: RelativeLayout = v.findViewById<View>(R.id.sliderRlt) as RelativeLayout
        title.text = items!![position].original_title
        desc.text = items!![position].overview
        val url: String = "http://image.tmdb.org/t/p/w500" + items!![position].backdrop_path
        Glide.with(act)
            .load(url)
            .placeholder(R.drawable.oops)
            .dontAnimate().into(image);
        layout.setOnClickListener(View.OnClickListener { v ->
            val bundle = Bundle()
            bundle.putString("MOVIE_ID", items!![position].id.toString())
            navController!!.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        })
        (container as ViewPager).addView(v)
        return v
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        (container as ViewPager).removeView(`object` as View)
    }
}