package com.origogi.gallery.presenter

import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlin.coroutines.CoroutineContext

class MainPresenter(private val view: MainContract.View) : MainContract.Presenter, CoroutineScope {

    lateinit var job: Job

    override fun init() {
        job = Job()
    }

    override fun deInit() {
        job.cancel()
    }

    override fun launch() {

        launch {
            val channel = ImageDataProvider().get()

            while (!channel.isClosedForReceive) {
                val imageData = channel.receive()
                withContext(Main) {
                    view.addItem(imageData)
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + Main
}