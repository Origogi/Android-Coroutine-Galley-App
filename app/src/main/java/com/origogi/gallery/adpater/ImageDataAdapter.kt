package com.origogi.gallery.adpater

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.origogi.gallery.R
import com.origogi.gallery.databinding.ListItemBinding
import com.origogi.gallery.model.ImageData


class ImageDataAdapter(private val context: Context) :
    RecyclerView.Adapter<ImageDataAdapter.ViewHolder>() {

    private val imageDataList = mutableListOf<ImageData>()

    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ImageData) {
            binding.item = data
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = DataBindingUtil.inflate<ListItemBinding>(LayoutInflater.from(parent.context),
            R.layout.list_item, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageData = imageDataList[position]

       holder.bind(imageData)

        //Preload
        if (position <= imageDataList.size) {
            val endPosition = if (position + 6 > imageDataList.size) {
                imageDataList.size
            } else {
                position + 6
            }
            imageDataList.subList(position, endPosition).map { it.imageUrl }.forEach {
                preload(context, it)
            }
        }
    }

    override fun getItemCount() = imageDataList.size

    fun add(imageData: ImageData) {
        imageDataList.add(imageData)
        notifyItemInserted(imageDataList.size)
    }

    fun preload(context: Context, url: String) {
        Glide.with(context).load(url)
            .preload(150, 150)
    }

    fun update(list: List<ImageData>) {
        val diffCallback = Diff(imageDataList, list)
        val diffResult = DiffUtil.calculateDiff(diffCallback)

        imageDataList.clear()
        imageDataList.addAll(list)
        diffResult.dispatchUpdatesTo(this)
    }

    private class Diff (
        private val oldItems: List<ImageData>,
        private val newItems: List<ImageData>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int =
            oldItems.size

        override fun getNewListSize(): Int =
            newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItems[oldItemPosition].imageUrl == newItems[newItemPosition].imageUrl
        }

        /**
         * 아이템을 서로 비교하는게 좋다.
         */
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]

            return oldItem == newItem
        }
    }
}