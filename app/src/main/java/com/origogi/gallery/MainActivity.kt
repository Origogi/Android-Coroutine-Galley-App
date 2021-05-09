package com.origogi.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.origogi.gallery.adpater.ImageDataAdapter
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import com.origogi.gallery.presenter.MainContract
import com.origogi.gallery.presenter.MainPresenter
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope, MainContract.View {
    lateinit var job: Job

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ImageDataAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager
    private lateinit var presenter: MainContract.Presenter

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        job = Job()

        presenter = MainPresenter(this).apply {
            init()
            launch()
        }

        viewManager = LinearLayoutManager(this)
        viewAdapter = ImageDataAdapter(this)
        recyclerView = this.findViewById<RecyclerView>(R.id.image_title_list).apply {
            layoutManager = viewManager
            adapter = viewAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
        presenter.deInit()
    }

    override fun addItem(imageData: ImageData) {
        launch {
            viewAdapter.add(imageData)
        }
    }
}