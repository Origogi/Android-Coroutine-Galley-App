package com.origogi.gallery.vm

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
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
            val channel = ImageDataProvider().get(viewModelScope)

            withContext(Main) {
                counter.value = 0
            }

            channel.consumeEach { imageData ->
                withContext(Main) {
                    counter.value = counter.value?.let { it + 1}
                    imageDataList.value = imageDataList.value?.let { it + imageData }
                }
            }
        }
    }

    fun getImageDataList() = imageDataList as LiveData<List<ImageData>>
    fun getCounter() = counter as LiveData<Int>
}