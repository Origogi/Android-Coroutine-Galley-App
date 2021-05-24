package com.origogi.gallery.presenter

import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
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
            val channel = ImageDataProvider().get(this)

            channel.consumeEach {
                count++
                withContext(Main) {
                    view.addItem(it)
                    view.updateCount(count)
                }
            }
        }
    }

    override val coroutineContext: CoroutineContext
        get() = job + newSingleThreadContext("Presenter")
}