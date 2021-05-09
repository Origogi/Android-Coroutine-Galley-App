package com.origogi.gallery.presenter

import com.origogi.gallery.model.ImageData

class MainContract {

    interface View {
        fun addItem(imageData: ImageData)
    }

    interface Presenter {
        fun launch()
        fun init()
        fun deInit()
    }
}