package com.origogi.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.ScriptGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.origogi.gallery.adpater.ImageDataAdapter
import com.origogi.gallery.model.ImageDataProvider
import com.origogi.gallery.vm.MyViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {
    lateinit var job: Job

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: ImageDataAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

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

        val viewModel = ViewModelProvider(this)[MyViewModel::class.java]

        viewModel.getImageDataList().observe(this, Observer {
            viewAdapter.update(it)
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }
}