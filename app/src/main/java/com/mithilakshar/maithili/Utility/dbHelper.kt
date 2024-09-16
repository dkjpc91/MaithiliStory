package com.mithilakshar.maithili.Utility


import android.annotation.SuppressLint
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.mithilakshar.maithili.Model.homeData
import com.mithilakshar.maithili.Model.nesteddata
import java.io.File


class dbHelper(context: Context, dbName: String) {

    private val TAG = "DBHelper"
    val dbFolderPath = context.getExternalFilesDir(null)?.absolutePath + File.separator + "test"
    val dbFilePath = "$dbFolderPath/$dbName"
    private var db: SQLiteDatabase? = null

    init {
        try {
            db = SQLiteDatabase.openDatabase(dbFilePath, null, SQLiteDatabase.OPEN_READWRITE)
        } catch (e: Exception) {
            Log.e(TAG, "Error opening database", e)
        }
    }

    fun getTableNames(): List<String> {
        val tableNames = mutableListOf<String>()
        db?.let {
            val cursor = it.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name != 'android_metadata'", null)
            try {
                cursor.use { c ->
                    while (c.moveToNext()) {
                        val tableName = c.getString(0)
                        tableNames.add(tableName)
                    }
                }
            } finally {
                cursor.close()  // Close the cursor even if an exception occurs
            }
        }
        return tableNames
    }


    @SuppressLint("Range")
    fun getColumnNames(tableName: String): List<String> {
        val columnNames = mutableListOf<String>()
        try {
            db?.let { db ->
                if (!db.isOpen) {
                    // Handle case where database is not open
                    return emptyList()
                }
                val cursor = db.rawQuery("PRAGMA table_info($tableName)", null)

                cursor.use { c ->
                    while (c.moveToNext()) {
                        val columnName = c.getString(c.getColumnIndex("name"))
                        columnNames.add(columnName)
                        Log.d(TAG, "Column Name: $columnName")
                    }
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching column names", e)
            // Handle exception (e.g., throw or return empty list)
        } finally {
            //db?.close()
        }
        return columnNames
    }



    fun getAllTableData(tableName: String): List<Map<String, Any?>> {
        val dataList = mutableListOf<Map<String, Any?>>()
        db?.let { database ->
            if (!database.isOpen) {
                Log.w(TAG, "Database not open for table: $tableName")
                return emptyList()
            }

            val cursor = database.rawQuery("SELECT * FROM $tableName", null)
            cursor.use { c ->
                val columnNames = getColumnNames(tableName)  // Reuse existing function for column names
                while (c.moveToNext()) {
                    val rowData = mutableMapOf<String, Any?>()
                    for (i in 0 until columnNames.size) {
                        val columnName = columnNames[i]
                        val value = c.getString(i)  // Assuming all columns are string for simplicity
                        rowData[columnName] = value
                    }
                    dataList.add(rowData)
                }
            }
        }
        return dataList
    }


    fun getRowCount(tableName: String): Int {
        var rowCount = 0
        db?.let {
            val cursor = it.rawQuery("SELECT COUNT(*) FROM $tableName", null)
            cursor.use { c ->
                if (c.moveToFirst()) {
                    rowCount = c.getInt(0)
                }
            }
        }
        return rowCount
    }

    fun getRowValues(tableName: String, primaryKeyValue: Any): List<Any>? {
        var rowValues: MutableList<Any>? = null // Use MutableList instead of List
        db?.let {
            val cursor = it.rawQuery("SELECT * FROM $tableName WHERE id = ?", arrayOf(primaryKeyValue.toString()))
            cursor.use { c ->
                if (c.moveToFirst()) {
                    rowValues = mutableListOf()
                    do {
                        for (i in 0 until c.columnCount) {
                            val value = when (c.getType(i)) {
                                Cursor.FIELD_TYPE_NULL -> null
                                Cursor.FIELD_TYPE_INTEGER -> c.getLong(i)
                                Cursor.FIELD_TYPE_FLOAT -> c.getDouble(i)
                                Cursor.FIELD_TYPE_STRING -> c.getString(i)
                                Cursor.FIELD_TYPE_BLOB -> c.getBlob(i)
                                else -> null
                            }
                            rowValues!!.add(value ?: "") // Add value to the mutable list
                        }
                    } while (c.moveToNext())
                }
            }
        }
        return rowValues
    }

    @SuppressLint("Range")
    fun getHomeDataList(): List<homeData> {
        val homeDataList = mutableListOf<homeData>()
        db?.let { database ->
            try {
                if (!database.isOpen) {
                    Log.w(TAG, "Database not open for reading home data")
                    return emptyList()
                }

                val query = "SELECT DISTINCT sno, category,ename, name, description, viewtype, avkey, imageurl,datakey FROM homepage"
                database.rawQuery(query, null)?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val snoIndex = cursor.getColumnIndexOrThrow("sno")
                        val categoryIndex = cursor.getColumnIndexOrThrow("category")
                        val enameIndex = cursor.getColumnIndexOrThrow("ename")
                        val nameIndex = cursor.getColumnIndexOrThrow("name")
                        val descriptionIndex = cursor.getColumnIndexOrThrow("description")
                        val viewtypeIndex = cursor.getColumnIndexOrThrow("viewtype")
                        val avkeyIndex = cursor.getColumnIndexOrThrow("avkey")
                        val imageurlIndex = cursor.getColumnIndexOrThrow("imageurl")
                        val datakeyIndex = cursor.getColumnIndexOrThrow("datakey")

                        val sno = cursor.getInt(snoIndex)
                        val category = cursor.getString(categoryIndex)
                        val ename = cursor.getString(enameIndex)
                        val name = cursor.getString(nameIndex)
                        val description = cursor.getString(descriptionIndex)
                        val viewtype = cursor.getString(viewtypeIndex)
                        val avkey = cursor.getString(avkeyIndex)
                        val imageurl = cursor.getInt(imageurlIndex)
                        val datakey = cursor.getInt(datakeyIndex)

                        homeDataList.add(homeData(sno, category,ename ,name, description, viewtype, avkey, imageurl,
                            datakey.toString()
                        ))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching home data", e)
                e.printStackTrace()
            }
        } ?: Log.e(TAG, "Database is null!")

        return homeDataList
    }


    @SuppressLint("Range")
    fun getUniqueHomeDataList(): List<homeData> {
        val homeDataList = mutableListOf<homeData>()
        val nameSet = mutableSetOf<String>()  // Set to keep track of unique names

        db?.let { database ->
            try {
                if (!database.isOpen) {
                    Log.w(TAG, "Database not open for reading home data")
                    return emptyList()
                }

                val query = "SELECT sno, category, ename,name, description, viewtype, avkey, imageurl, datakey FROM homepage"
                database.rawQuery(query, null)?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val snoIndex = cursor.getColumnIndexOrThrow("sno")
                        val categoryIndex = cursor.getColumnIndexOrThrow("category")
                        val enameIndex = cursor.getColumnIndexOrThrow("ename")
                        val nameIndex = cursor.getColumnIndexOrThrow("name")
                        val descriptionIndex = cursor.getColumnIndexOrThrow("description")
                        val viewtypeIndex = cursor.getColumnIndexOrThrow("viewtype")
                        val avkeyIndex = cursor.getColumnIndexOrThrow("avkey")
                        val imageurlIndex = cursor.getColumnIndexOrThrow("imageurl")
                        val datakeyIndex = cursor.getColumnIndexOrThrow("datakey")

                        val sno = cursor.getInt(snoIndex)
                        val category = cursor.getString(categoryIndex)
                        val ename = cursor.getString(enameIndex)
                        val name = cursor.getString(nameIndex)
                        val description = cursor.getString(descriptionIndex)
                        val viewtype = cursor.getString(viewtypeIndex)
                        val avkey = cursor.getString(avkeyIndex)
                        val imageurl = cursor.getInt(imageurlIndex)
                        val datakey = cursor.getString(datakeyIndex)

                        // Check if the name is already in the set
                        if (name !in nameSet) {
                            nameSet.add(name)  // Add name to the set
                            homeDataList.add(homeData(sno, category,ename, name, description, viewtype, avkey, imageurl,
                                datakey
                            ))
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching home data", e)
                e.printStackTrace()
            }
        } ?: Log.e(TAG, "Database is null!")

        return homeDataList
    }



    @SuppressLint("Range")
    fun getHomenestedDataList( name: String): List<nesteddata> {
        val nesteddata = mutableListOf<nesteddata>()
        db?.let { database ->
            try {
                if (!database.isOpen) {
                    Log.w(TAG, "Database not open for reading home data")
                    return emptyList()
                }

                val query = "SELECT DISTINCT sno, category,name, imageurl, audiourl, videourl, avkey,layoutkey,description,story FROM $name"
                database.rawQuery(query, null)?.use { cursor ->
                    while (cursor.moveToNext()) {
                        val snoIndex = cursor.getColumnIndexOrThrow("sno")
                        val categoryIndex = cursor.getColumnIndexOrThrow("category")

                        val nameIndex = cursor.getColumnIndexOrThrow("name")
                        val imageurlIndex = cursor.getColumnIndexOrThrow("imageurl")
                        val audiourlIndex = cursor.getColumnIndexOrThrow("audiourl")
                        val videourlIndex = cursor.getColumnIndexOrThrow("videourl")
                        val avkeyIndex = cursor.getColumnIndexOrThrow("avkey")
                        val layoutkeyIndex = cursor.getColumnIndexOrThrow("layoutkey")
                        val descriptionIndex = cursor.getColumnIndexOrThrow("description")
                        val storyIndex = cursor.getColumnIndexOrThrow("story")

                        val sno = cursor.getInt(snoIndex)
                        val category = cursor.getString(categoryIndex)

                        val name = cursor.getString(nameIndex)
                        val imageurl = cursor.getString(imageurlIndex)
                        val audiourl = cursor.getString(audiourlIndex)
                        val videourl = cursor.getString(videourlIndex)
                        val avkey = cursor.getString(avkeyIndex)
                        val layoutkey = cursor.getInt(layoutkeyIndex)
                        val description = cursor.getInt(descriptionIndex)
                        val story = cursor.getInt(storyIndex)

                        nesteddata.add(nesteddata(sno, category, name,imageurl, audiourl, videourl, avkey,
                            layoutkey.toString(),
                            description.toString(),
                            story.toString()
                        ))
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching home data", e)
                e.printStackTrace()
            }
        } ?: Log.e(TAG, "Database is null!")

        return nesteddata
    }
















}