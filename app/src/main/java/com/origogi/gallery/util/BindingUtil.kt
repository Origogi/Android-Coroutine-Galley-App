package com.origogi.gallery.util

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.origogi.gallery.adpater.ImageDataAdapter
import com.origogi.gallery.model.ImageData

@BindingAdapter("bindItem")
fun bindItem(recyclerView: RecyclerView, items:LiveData<List<ImageData>>){
    val adapter = recyclerView.adapter

    if (adapter is ImageDataAdapter) {
        items.value?.let {
            println("bind item ${it.size}")
            adapter.update(it)
        }
    }

}