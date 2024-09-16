package com.mithilakshar.maithili.Utility

import android.util.Log
import com.mithilakshar.maithili.Repository.Filefromurldownloader
import com.mithilakshar.maithili.Room.UpdatesDao
import kotlinx.coroutines.CoroutineScope

class dbDownloadersequenceurl(
    private val updatesDao: UpdatesDao,
    private val filefromurldownloader: Filefromurldownloader
) {

    fun observeMultipleFileExistence(
        filesWithIds: List<Pair<String, String>>,
        coroutineScope: CoroutineScope,
        progressCallback: (Int, String) -> Unit,
        onComplete: () -> Unit
    ) {
        val totalFiles = filesWithIds.size
        var filesProcessed = 0

        fun downloadNextFile(index: Int) {
            if (index >= totalFiles) {
                // All files processed
                Log.d("dbd", "All files processed successfully")
                onComplete() // Call onComplete callback here
                return
            }

            val (fileurl, filename) = filesWithIds[index]
            Log.d("dbd", "Processing file: $filename with url ID: $fileurl")

            // File path where the file should be stored
            val localFileName = "$filename.db"

            // Proceed to download the file (fresh copy)
            val storagePath = "$fileurl"
            downloadFile(storagePath, localFileName, progressCallback) {
                // Proceed to the next file after the current one has been downloaded
                filesProcessed++
                downloadNextFile(index + 1)
            }
        }

        downloadNextFile(0) // Start processing files
    }

    private fun downloadFile(
        storagePath: String,
        localFileName: String,
        progressCallback: (Int, String) -> Unit,
        onComplete: () -> Unit
    ) {
        // Trigger file download without checking file existence
        filefromurldownloader.retrieveURL(storagePath, localFileName) { downloadedFile ->
            Log.d("dbd", "Downloaded file path: $downloadedFile")
            progressCallback(100, localFileName) // Notify progress
            onComplete() // Notify that the download is complete
        }
    }
}
