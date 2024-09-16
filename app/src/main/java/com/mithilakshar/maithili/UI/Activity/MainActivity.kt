package com.mithilakshar.maithili.UI.Activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.mithilakshar.maithili.Adapter.HomeAdapter

import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.Model.nesteddata
import com.mithilakshar.maithili.R
import com.mithilakshar.maithili.Repository.Filefromurldownloader
import com.mithilakshar.maithili.Repository.FirebaseFileDownloader
import com.mithilakshar.maithili.Repository.firestoreRepository
import com.mithilakshar.maithili.Room.UpdatesDao
import com.mithilakshar.maithili.Room.UpdatesDatabase
import com.mithilakshar.maithili.Utility.InAppUpdate
import com.mithilakshar.maithili.Utility.NetworkDialog
import com.mithilakshar.maithili.Utility.NetworkLiveCheck
import com.mithilakshar.maithili.Utility.UpdateChecker
import com.mithilakshar.maithili.Utility.dbDownloadersequence
import com.mithilakshar.maithili.Utility.dbDownloadersequenceurl
import com.mithilakshar.maithili.Utility.dbHelper
import com.mithilakshar.maithili.ViewModel.homeViewModel
import com.mithilakshar.maithili.databinding.ActivityHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityHomeBinding
    val repository: firestoreRepository= firestoreRepository()
    private val inAppUpdateManager: InAppUpdate by lazy {
        InAppUpdate(this, MY_REQUEST_CODE)
    }
    private val MY_REQUEST_CODE = 123

    val viewModelhome: homeViewModel by lazy {
        ViewModelProvider(this).get(homeViewModel::class.java)
    }

    private lateinit var networkLiveCheck: NetworkLiveCheck

    private var completedFiles = 0
    private var totalFiles = 0

    private lateinit var updatesDao: UpdatesDao
    private lateinit var fileDownloader: FirebaseFileDownloader
    private lateinit var filefromurldownloader: Filefromurldownloader
    private lateinit var dbDownloadersequence: dbDownloadersequence
    private lateinit var dbDownloadersequenceurl: dbDownloadersequenceurl

    private lateinit var dbhomepagehelper: dbHelper
    var isUpdateNeeded="b"

    var bannerurls: List<String> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //networkcheck
        networkLiveCheck = NetworkLiveCheck(this)
        val networkStatusDialog = NetworkDialog(this)
        networkLiveCheck.observe(this, Observer{

            if (!it)
            {networkStatusDialog.show() }
            else
            {
                networkStatusDialog.dismiss()
            }
        })
        //networkcheckend

        //masterupdate
        fileDownloader = FirebaseFileDownloader(this)
        filefromurldownloader = Filefromurldownloader(this)
        updatesDao = UpdatesDatabase.getDatabase(applicationContext).UpdatesDao()
        dbDownloadersequence = dbDownloadersequence(updatesDao, fileDownloader)
        dbDownloadersequenceurl=dbDownloadersequenceurl(updatesDao, filefromurldownloader)

        val filesWithIds = listOf(
            Pair("homepage", 1),

        )

        lifecycleScope.launch {
            val updateChecker = UpdateChecker(updatesDao)
            isUpdateNeeded = updateChecker.getUpdateStatus()
            Log.d("rawupdate", " :  isUpdateNeededraw $isUpdateNeeded")
            if (isUpdateNeeded!="a") {

                Log.d("updatechecker", " :  needed $isUpdateNeeded")
                Log.d("updatechecker", " :  needed ${!checkFileExistence(this@MainActivity,"homepage.db")}")

                dbDownloadersequence.observeMultipleFileExistence(
                    filesWithIds,
                    this@MainActivity,
                    lifecycleScope,
                    homeActivity = this@MainActivity, // Your activity
                    progressCallback = { progress, filePair  ->
                        Log.d("updatechecker", " :  progress $progress  filePair $filePair")





                    },{

                        Log.d("updatechecker", " :  downloadcompleted")

                        dbhomepagehelper=dbHelper(context =this@MainActivity, dbName = "homepage.db")
                        val homelist=dbhomepagehelper.getUniqueHomeDataList()
                        val filesWithIds = homelist.map { Pair(it.datakey, it.ename) }
                        startProcessing(filesWithIds, isUpdateNeeded)
                        val nestedDataMap: Map<Int, List<nesteddata>> = mapOf(
                            0 to listOf(
                                nesteddata(1, "Category1", "Item1", "imageUrl1", "audioUrl1", "videoUrl1", "avKey1", "layoutKey1", "Description1", "Story1"),
                                nesteddata(2, "Category1", "Item2", "imageUrl2", "audioUrl2", "videoUrl2", "avKey2", "layoutKey2", "Description2", "Story2")
                            ),
                            1 to listOf(
                                nesteddata(3, "Category2", "Item3", "imageUrl3", "audioUrl3", "videoUrl3", "avKey3", "layoutKey3", "Description3", "Story3")
                            )
                            // Add more entries as needed
                        )

                        val adapter= HomeAdapter(homelist,this@MainActivity,dbhomepagehelper,dbDownloadersequenceurl,isUpdateNeeded, nestedDataProvider = { position ->
                            // Provide nested data for the specific position
                            nestedDataMap[position] ?: emptyList()
                        })
                        binding.homeRecycler.adapter=adapter
                        binding.homeRecycler.visibility= View.VISIBLE



                        Log.d("dbhelper", "File: ${homelist.joinToString(separator = ", ") { it.toString() }}")



                    }
                )


            } else {


                Log.d("updatechecker", " :  update not needed.")




                dbhomepagehelper=dbHelper(context =this@MainActivity, dbName = "homepage.db")
                val homelist=dbhomepagehelper.getUniqueHomeDataList()
                val filesWithIds = homelist.map { Pair(it.datakey, it.ename) }
                startProcessing(filesWithIds, isUpdateNeeded)


                val nestedDataMap: Map<Int, List<nesteddata>> = mapOf(
                    0 to listOf(
                        nesteddata(1, "Category1", "Item1", "imageUrl1", "audioUrl1", "videoUrl1", "avKey1", "layoutKey1", "Description1", "Story1"),
                        nesteddata(2, "Category1", "Item2", "imageUrl2", "audioUrl2", "videoUrl2", "avKey2", "layoutKey2", "Description2", "Story2")
                    ),
                    1 to listOf(
                        nesteddata(3, "Category2", "Item3", "imageUrl3", "audioUrl3", "videoUrl3", "avKey3", "layoutKey3", "Description3", "Story3")
                    )
                    // Add more entries as needed
                )

                val adapter= HomeAdapter(homelist,this@MainActivity,dbhomepagehelper,dbDownloadersequenceurl,isUpdateNeeded, nestedDataProvider = { position ->
                    // Provide nested data for the specific position
                    nestedDataMap[position] ?: emptyList()
                })
                binding.homeRecycler.adapter=adapter
                binding.homeRecycler.visibility= View.VISIBLE




            }
        }






        lifecycleScope.launch {

            bannerurls=viewModelhome.getBannerurlList("a")

            val random = Random.nextInt(bannerurls.size)

            Glide.with(this@MainActivity).load(bannerurls.get(random)).into(binding.homeBanner)






            binding.homeBanner.setOnClickListener {

                val intent= Intent(this@MainActivity, CategoryActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("dataKey","video")
                intent.putExtra("avKey","0")
                this@MainActivity.startActivity(intent)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)

            }

        }





    }




    override fun onResume() {
        super.onResume()

        inAppUpdateManager.checkForAppUpdate()
    }

    override fun onDestroy() {
        super.onDestroy()

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        inAppUpdateManager.handleActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun checkFileExistence(context: Context, localFileName: String): Boolean {
        val downloadDirectory = File(context.getExternalFilesDir(null), "test")

        if (!downloadDirectory.exists()) {
            downloadDirectory.mkdirs()
        }

        val localFile = File(downloadDirectory, localFileName)

        return localFile.exists()
    }








    fun startProcessing(homelist: List<Pair<String, String>>, isUpdateNeeded: String) {
        totalFiles = homelist.size // Set the total number of files to process
        completedFiles = 0 // Reset completedFiles before starting

        CoroutineScope(Dispatchers.Main).launch {
            for ((fileId, fileName) in homelist) {
                processFile(fileId, fileName, isUpdateNeeded,
                    onProgress = { progress, filename ->
                        Log.d("startProcessing", "fileName: $filename - Progress: $progress")
                    },
                    onComplete = {
                        Log.d("startProcessing", "startProcessing: completed")
                    }
                )
            }
        }
    }
    private suspend fun processFile(
        fileId: String,
        fileName: String,
        isUpdateNeeded: String,
        onProgress: (Int, String) -> Unit,
        onComplete: () -> Unit
    ) {
        Log.d("rawupdate", "Processing file: $fileId - $fileName")

        val fileExists = withContext(Dispatchers.IO) {
            checkFileExistence(this@MainActivity, "$fileName.db")
        }
        Log.d("rawupdate", "Checking file existence for $fileName: $fileExists")

        if (!fileExists || isUpdateNeeded != "a") {
            Log.d("rawupdate", "File needs download or update: $fileId")

            // Use the IO Dispatcher for downloading
            val ioScope = CoroutineScope(Dispatchers.IO)

            dbDownloadersequenceurl.observeMultipleFileExistence(
                listOf(Pair(fileId, fileName)), // List of one file to process
                ioScope, // Pass the CoroutineScope
                { progress, filename ->
                    // Update UI on progress
                    CoroutineScope(Dispatchers.Main).launch {
                        onProgress(progress, filename)
                        Log.d("rawupdate", "Downloading file: $filename - Progress: $progress")
                    }
                },
                {
                    // Handle download completion
                    CoroutineScope(Dispatchers.Main).launch {
                        completedFiles++
                        Log.d("rawupdate", "Download complete for: $fileId")

                        if (completedFiles == totalFiles) {
                            Log.d("rawupdate", "All downloads completed")
                            onComplete()
                        }
                    }
                }
            )
        } else {
            // File exists and no update needed
            withContext(Dispatchers.Main) {
                completedFiles++
                onProgress(100, fileName) // Assume 100% progress if file exists and no update needed
                Log.d("rawupdate", "File exists and no update needed: $fileId")

                if (completedFiles == totalFiles) {
                    Log.d("rawupdate", "All downloads completed")
                    onComplete()
                }
            }
        }
    }





}