package com.mithilakshar.maithili.Utility

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.mithilakshar.maithili.R

class NetworkDialog(context: Context): AlertDialog(context) {


    init {

        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.networkdialog, null)
        setView(view)
        setCancelable(false)
    }
}