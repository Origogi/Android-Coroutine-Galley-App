package com.origogi.gallery

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.origogi.gallery.provider.ImageDataProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        GlobalScope.launch {
            val channel = ImageDataProvider().get()

            while(!channel.isClosedForReceive) {
                println(channel.receive())
            }
        }
    }
}