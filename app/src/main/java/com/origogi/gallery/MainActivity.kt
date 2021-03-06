package com.origogi.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.origogi.gallery.adpater.ImageDataAdapter
import com.origogi.gallery.model.EndData
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ImageDataAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private var dataCount = 0

    override val coroutineContext: CoroutineContext
        get() = Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        job = Job()

        viewManager = LinearLayoutManager(this)
        viewAdapter = ImageDataAdapter(this)
        recyclerView = this.findViewById<RecyclerView>(R.id.image_title_list).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }

        launch {
            dataCount = 0
            findViewById<TextView>(R.id.counter).text = "Image Count : $dataCount"

            val channel = ImageDataProvider().get()

            while (!channel.isClosedForReceive) {

                when (val data = channel.receive()) {
                    is ImageData -> withContext(Main) {
                        dataCount++
                        findViewById<TextView>(R.id.counter).text = "Image Count : $dataCount"
                        viewAdapter.add(data)
                    }
                    is EndData -> channel.cancel()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}