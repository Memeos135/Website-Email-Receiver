package com.gama.emailreceiver.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gama.emailreceiver.MainActivity
import com.gama.emailreceiver.R
import com.gama.emailreceiver.utils.Constants
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class FCMInstance: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d("TOKEN UPDATED", p0)
        // update the stored token
        PreferenceManager.getDefaultSharedPreferences(this).edit().putString(Constants.FCM_TOKEN, p0).commit()
        // send it to server

    }



    override fun onMessageReceived(p0: RemoteMessage) {
        super.onMessageReceived(p0)
        showNotification(p0.notification!!.title, p0.notification!!.body)
    }

    private fun showNotification(title: String?, body: String?) {
        val notificationManager: NotificationManager? = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationChannel = "com.gama.emailreceiver.test"

        // if app is in foreground
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel1 = NotificationChannel(notificationChannel, "Notification", NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel1.description = "EmailReceiver Channel"
            notificationChannel1.enableLights(true)
            notificationChannel1.lightColor = Color.BLUE

            notificationManager!!.createNotificationChannel(notificationChannel1)

        }
        // if app is in background / killed
        val notificationBuilder = NotificationCompat.Builder(this, notificationChannel)
        notificationBuilder
            .setAutoCancel(true)
            .setDefaults(Notification.DEFAULT_ALL)
            .setWhen(System.currentTimeMillis())
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentInfo(body)
            .setContentIntent(PendingIntent.getActivity(this, 0, Intent(this, MainActivity::class.java), PendingIntent.FLAG_UPDATE_CURRENT))

        notificationManager!!.notify(Random.nextInt(), notificationBuilder.build())
    }

}