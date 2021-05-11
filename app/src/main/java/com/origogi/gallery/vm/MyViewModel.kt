package com.origogi.gallery.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.gallery.model.EndData
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {

    private val imageDataList: MutableLiveData<List<ImageData>> =
        MutableLiveData<List<ImageData>>().apply {
            viewModelScope.launch(Main) {
                value = mutableListOf()
            }
        }

    private val counter: MutableLiveData<Int> = MutableLiveData<Int>().apply {
        viewModelScope.launch(Main) {
            value = 0
        }
    }

    init {
        load()
    }

    fun load() {

        viewModelScope.launch(Dispatchers.IO) {
            val channel = ImageDataProvider().get()

            withContext(Main) {
                counter.value = 0
            }

            while (!channel.isClosedForReceive) {
                when (val data = channel.receive()) {
                    is ImageData -> {
                        withContext(Main) {
                            imageDataList.value = (imageDataList.value!! + data)
                            counter.value = 1 + counter.value!!
                        }
                    }
                    is EndData -> break
                }


            }
        }
    }

    fun getImageDataList() = imageDataList as LiveData<List<ImageData>>
    fun getCounter() = counter as LiveData<Int>
}