package com.origogi.gallery.vm

import android.widget.TextView
import androidx.lifecycle.*
import com.origogi.gallery.model.ImageData
import com.origogi.gallery.model.ImageDataProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MyViewModel : ViewModel() {

    private val imageDataList = liveData {
        val itemList = mutableListOf<ImageData>()
        val channel = ImageDataProvider().get(viewModelScope)
        channel.consumeEach {
            itemList.add(it)
            emit(itemList)
        }

    }

    private val counter = Transformations.map(imageDataList) {
        it.size
    }

    fun getImageDataList() = imageDataList as LiveData<List<ImageData>>
    fun getCounter() = counter as LiveData<Int>
}