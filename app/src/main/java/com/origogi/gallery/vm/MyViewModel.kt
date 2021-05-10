package com.origogi.gallery.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {
    private val imageDataList: MutableLiveData<List<ImageData>> = MutableLiveData<List<ImageData>>().apply {
        viewModelScope.launch(Main) {
            value = mutableListOf()
        }
    }

    init {
        load()
    }

    fun load() {

        viewModelScope.launch(Dispatchers.IO) {
            val channel = ImageDataProvider().get()

            while (!channel.isClosedForReceive) {
                withContext(Main) {
                    imageDataList.value = (imageDataList.value!! + channel.receive())
                }
            }
        }
    }

    fun getImageDataList() = imageDataList as LiveData<List<ImageData>>
}