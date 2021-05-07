package com.origogi.gallery.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.origogi.gallery.R
import com.origogi.gallery.model.ImageData
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ImageDataAdapter(private val context: Context) : RecyclerView.Adapter<ImageDataAdapter.ViewHolder>() {

    private val imageDataList = mutableListOf<ImageData>()

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val title: TextView = itemView.findViewById(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val convertView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(convertView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageData = imageDataList[position]

        holder.apply {
            Glide.with(context).load(imageData.imageUrl)
                .override(150, 150)
                .into(image)
            title.text = imageData.imageTitle
        }

        //Preload
        if (position <= imageDataList.size) {
            val endPosition = if (position + 6 > imageDataList.size) {
                imageDataList.size
            } else {
                position + 6
            }
            imageDataList.subList(position, endPosition ).map { it.imageUrl }.forEach {
                preload(context, it)
            }
        }
    }

    override fun getItemCount() = imageDataList.size

    fun add(imageData: ImageData) {
        imageDataList.add(imageData)

        GlobalScope.launch(Main) {
            notifyItemInserted(imageDataList.size)
        }

    }

    fun preload(context: Context,  url : String) {
        Glide.with(context).load(url)
            .preload(150, 150)
    }
}