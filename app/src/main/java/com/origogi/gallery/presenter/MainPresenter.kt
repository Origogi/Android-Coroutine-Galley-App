package com.origogi.gallery.presenter

import com.origogi.gallery.model.EndData
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter, CoroutineScope {

    lateinit var job: Job
    var count = 0

    override fun init() {
        job = Job()
        count = 0
    }

    override fun deInit() {
        job.cancel()
    }

    override fun launch() {

        count = 0
        launch {
            println("Thread : " + Thread.currentThread().name)
            val channel = ImageDataProvider().get()

            while (!channel.isClosedForReceive) {
                val data = channel.receive()
                count++
                when(data) {
                    is ImageData -> {
                        view.addItem(data)
                        view.updateCount(count)
                    }
                    is EndData -> channel.cancel()
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + newSingleThreadContext("Presenter")
}