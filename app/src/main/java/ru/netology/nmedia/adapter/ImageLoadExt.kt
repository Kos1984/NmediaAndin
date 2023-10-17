package ru.netology.nmedia.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R

fun ImageView.load(url:String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.ic_like_filled_24dp)
        .error(R.drawable.baseline_error_100dp)
        .timeout(30_000)
        .into(this)
}