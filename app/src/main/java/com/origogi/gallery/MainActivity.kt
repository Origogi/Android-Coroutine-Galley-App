package com.origogi.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.origogi.gallery.adpater.ImageDataAdapter
import com.origogi.gallery.provider.ImageDataProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ImageDataAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewManager = LinearLayoutManager(this)
        viewAdapter = ImageDataAdapter(this)
        recyclerView = this.findViewById<RecyclerView>(R.id.image_title_list).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        GlobalScope.launch {
            val channel = ImageDataProvider().get()

            while (!channel.isClosedForReceive) {
                val imageData = channel.receive()
                println(imageData)
                viewAdapter.add(imageData)
            }
        }
    }
}