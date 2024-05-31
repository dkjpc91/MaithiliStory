package com.mithilakshar.maithili.Utility


import android.app.NotificationManager

import android.content.Context

import androidx.core.app.NotificationCompat


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

import com.mithilakshar.maithili.R





    class MyFirebaseMessagingService : FirebaseMessagingService() {

        override fun onMessageReceived(message: RemoteMessage) {
            super.onMessageReceived(message)
            val notificationData = message.notification

            getfirebasemessage(applicationContext,notificationData?.title.toString(),notificationData?.body.toString())

        }

        fun getfirebasemessage (context: Context, title:String, msg:String ){

            val notificationBuilder = NotificationCompat.Builder(this,"notification").setSmallIcon(R.drawable.notificationlogo)
                .setContentTitle(title).setContentTitle(msg).setAutoCancel(true)

            val manager =getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


            manager.notify(13, notificationBuilder.build())

        }




    }