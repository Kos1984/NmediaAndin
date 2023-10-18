package ru.netology.nmedia.adapter

import android.widget.ImageView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R

fun ImageView.load(url:String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.non_avatar_100dp)
        .error(R.drawable.baseline_error_100dp)
        .timeout(30_000)
        .circleCrop()
        .into(this)
}

fun ImageView.loadAttach(url: String){
    Glide.with(this)
        .load(url)
        .placeholder(R.drawable.baseline_downloading_24)
        .error(R.drawable.baseline_error_100dp)
        .timeout(30_000)
        .into(this)
}