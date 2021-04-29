package com.origogi.gallery.image

import android.content.Context
import com.bumptech.glide.Glide

object GlideImagePreload {

    fun fetch(context: Context, url: String) {
        Glide.with(context).load(url)
            .preload(150, 150)
    }

}