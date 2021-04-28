package com.origogi.gallery.image

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

object GlideImagePreload {

    private val dispatcher = newFixedThreadPoolContext(4, "ImagePreload")

    fun fetch(context: Context, urls: List<String>) {
        GlobalScope.launch(dispatcher) {
            urls.forEach {
                Glide.with(context).load(it).diskCacheStrategy(DiskCacheStrategy.ALL)
                    .preload(150, 150)
                println("preload $it")
            }
        }
    }
}