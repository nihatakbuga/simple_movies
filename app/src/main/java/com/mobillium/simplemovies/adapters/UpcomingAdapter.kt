package com.mobillium.simplemovies.adapters

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mobillium.simplemovies.R
import com.mobillium.simplemovies.response.upcoming.Result

class UpcomingAdapter : RecyclerView.Adapter<UpcomingAdapter.UpcomingListHolder> {
    private var upcomingResponseList: List<Result>? = null
    private var mContext: Activity? = null
    private var mInflater: LayoutInflater? = null
    private var layoutId = 0
    private var navController: NavController? = null

    constructor(
        mContext: Activity?,
        navController: NavController,
        upcomingResponseList: List<Result>?,
        mInflater: LayoutInflater?,
        layoutId: Int
    ) : super() {
        this.upcomingResponseList = upcomingResponseList
        this.mContext = mContext
        this.navController = navController
        this.mInflater = mInflater
        this.layoutId = layoutId
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingListHolder {
        val view = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
        return UpcomingListHolder(view)
    }

    override fun onBindViewHolder(holder: UpcomingListHolder, position: Int) {
        holder.title.text = upcomingResponseList!![position].original_title
        holder.desc.text = upcomingResponseList!![position].overview
        holder.date.text = upcomingResponseList!![position].release_date
        val url: String =
            "http://image.tmdb.org/t/p/w500" + upcomingResponseList!![position].backdrop_path
        Glide.with(mContext)
            .load(url)
            .placeholder(R.drawable.oops)
            .dontAnimate().into(holder.imageView);
        holder.newConstraintLayout.setOnClickListener(View.OnClickListener {
            val bundle = Bundle()
            bundle.putString("MOVIE_ID", upcomingResponseList!![position].id.toString())
            navController!!.navigate(R.id.action_homeFragment_to_detailFragment, bundle)
        })
    }

    override fun getItemCount(): Int {
        return upcomingResponseList!!.size
    }

    class UpcomingListHolder(rootView: View) :
        RecyclerView.ViewHolder(rootView) {
        val newConstraintLayout: ConstraintLayout
        val imageView: ImageView
        val title: TextView
        val desc: TextView
        val date: TextView

        init {
            newConstraintLayout = rootView.findViewById(R.id.message_list_layout)
            imageView = rootView.findViewById(R.id.upcoming_image)
            title = rootView.findViewById(R.id.upcoming_title)
            desc = rootView.findViewById(R.id.upcoming_desc)
            date = rootView.findViewById(R.id.upcoming_date)
        }
    }

}