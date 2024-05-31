package com.mithilakshar.maithili.Utility

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat


import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.mithilakshar.maithili.R

import com.mithilakshar.maithili.UI.Activity.PlayerActivity
import java.net.URL


class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        val notificationData = message.notification
        if (notificationData != null) {
            val title = notificationData.title.toString()
            val body = notificationData.body.toString()
            val imageUrl = notificationData.imageUrl.toString()

            getFirebaseMessage(this, title, body,imageUrl)
        }
    }
 }

    fun getFirebaseMessage(context: Context, title: String, body: String,imageUrl: String?) {
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Check for notification permission
        if (ContextCompat.checkSelfPermission(
                context,
               android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request permission if not granted
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.POST_NOTIFICATIONS),
                13
            )
            return
        }

        val channelId = "maithili"
        val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val notificationBuilder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .setSound(defaultSoundUri)
            .setContentIntent(getPendingIntent(context))

        if (imageUrl != null){
            val bitmap = getBitmapFromUrl(imageUrl)
            if (bitmap != null) {
                val bigPictureStyle = NotificationCompat.BigPictureStyle().bigPicture(bitmap)

                notificationBuilder.setStyle(bigPictureStyle)

            }
        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "maithili",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, notificationBuilder.build())
    }

    private fun getPendingIntent(context: Context): PendingIntent {
        val intent = Intent(context, PlayerActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)
    }

private fun getBitmapFromUrl(imageUrl: String): Bitmap? {
    try {
        val url = URL(imageUrl)
        val connection = url.openConnection()
        connection.doInput = true
        connection.connect()
        val inputStream = connection.inputStream
        return BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return null
}




