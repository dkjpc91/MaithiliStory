package com.mithilakshar.maithili.Repository

import android.content.Context
import android.util.Log
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

class Filefromurldownloader(private val context: Context) {

    private val TAG = "FileDownloader"
    private val client = OkHttpClient()

    fun retrieveURL(url: String, localFileName: String, callback: (File?) -> Unit) {
        // Create a directory for storing downloaded files
        val downloadDirectory = File(context.getExternalFilesDir(null), "test")
        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }

        // Create a local file path
        val localFile = File(downloadDirectory, localFileName)

        // Download the file from the URL directly, without checking if it exists
        downloadFile(url, localFile, callback)
    }

    private fun downloadFile(url: String, localFile: File, callback: (File?) -> Unit) {
        val request = Request.Builder()
            .url(url)
            .build()

        val call = client.newCall(request)

        call.enqueue(object : okhttp3.Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e(TAG, "Download failed", e)
                callback(null)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                if (response.isSuccessful) {
                    response.body?.let { body ->
                        saveFile(body, localFile, callback)
                    } ?: run {
                        Log.e(TAG, "Response body is null")
                        callback(null)
                    }
                } else {
                    Log.e(TAG, "Download failed: ${response.code}")
                    callback(null)
                }
            }
        })
    }

    private fun saveFile(body: ResponseBody, localFile: File, callback: (File?) -> Unit) {
        try {
            val inputStream: InputStream = body.byteStream()
            val outputStream: OutputStream = FileOutputStream(localFile)

            val buffer = ByteArray(1024)
            var bytesRead: Int
            var totalBytesRead = 0L
            val totalBytes = body.contentLength()

            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                outputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
                val progress = (100.0 * totalBytesRead / totalBytes).toInt()
                // You can post the progress updates here if needed
            }

            outputStream.flush()
            outputStream.close()
            inputStream.close()

            callback(localFile)
        } catch (e: Exception) {
            Log.e(TAG, "Error saving file", e)
            callback(null)
        }
    }
}
