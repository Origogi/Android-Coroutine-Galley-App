package com.origogi.gallery.model

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext

import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.regex.Pattern

class ImageDataProvider {
    private val baseUrl = "https://www.gettyimagesgallery.com/collection/sasha/"

    private val dispatcher = newFixedThreadPoolContext(4, "network")

    fun get(): ReceiveChannel<Data> {

        val channel = Channel<Data>(Channel.UNLIMITED)

        GlobalScope.launch(dispatcher) {
            parse(channel)
        }
        return channel
    }

    private suspend fun parse(channel: SendChannel<Data>) {
        try {
            val url = URL(baseUrl)

            val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

            if (conn != null) {
                conn.connectTimeout = 2000;
                conn.useCaches = false;
                if (conn.responseCode
                    == HttpURLConnection.HTTP_OK
                ) {
                    //    데이터 읽기
                    val br = BufferedReader(InputStreamReader(conn.inputStream, "euc-kr"))
                    br.lineSequence()
                        .filter { it.contains("img class=") }
                        .forEach {

                            val pattern = Pattern.compile("\"(.*?)\"")

                            val matcher = pattern.matcher(it)

                            var index = 0

                            var imageUrl = ""
                            var title = ""

                            while (matcher.find()) {
                                val token = (matcher.group(1))
                                if (index == 1) {
                                    imageUrl = token
                                } else if (index == 2) {
                                    title = token
                                    val imageData =
                                        ImageData(imageUrl = imageUrl, imageTitle = title)
                                    channel.send(imageData)
                                }
                                index += 1
                            }
                        }
                    br.close(); // 스트림 해제
                }
                println("=========end=========")
                conn.disconnect() // 연결 끊기
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }
        finally {
            channel.send(EndData(null))
        }

    }


}