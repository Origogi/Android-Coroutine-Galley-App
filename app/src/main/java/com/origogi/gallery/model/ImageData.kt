package com.origogi.gallery.model

sealed class Data

data class ImageData(val imageUrl : String, val imageTitle : String) : Data()
object EndData : Data()