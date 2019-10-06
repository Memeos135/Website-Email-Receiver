package com.gama.emailreceiver.services

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.AsyncTask
import android.os.Build
import android.preference.PreferenceManager
import android.util.Log
import androidx.core.app.NotificationCompat
import com.gama.emailreceiver.MainActivity
import com.gama.emailreceiver.R
import com.gama.emailreceiver.models.EmailModel
import com.gama.emailreceiver.room.DatabaseInstance
import com.gama.emailreceiver.utils.Constants
import com.gama.emailreceiver.web_services.ServicePost
import com.gama.emailreceiver.web_services.response_models.FetchAllResponseModel
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.lang.ref.WeakReference
import kotlin.random.Random

class FCMInstance: FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        super.onNewToken(p0)
        Log.d(Constants.TOKEN_UPDATED, p0)
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
        val notificationChannel = Constants.CHANNEL

        // if app is in foreground
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel1 = NotificationChannel(notificationChannel, Constants.NOTIFICATION, NotificationManager.IMPORTANCE_DEFAULT)
            notificationChannel1.description = Constants.CHANNEL_DESCRIPTION
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

        // sync data > fetch the whole list and filter those who are already stored locally
        // add the remaining ones to the local storage record
        FetchAllEmailsAsyncTask(this).execute()
    }
    class FetchAllEmailsAsyncTask(context: Context): AsyncTask<Void, Void, FetchAllResponseModel>(){
        private var weakReference: WeakReference<Context> = WeakReference(context)

        override fun doInBackground(vararg p0: Void?): FetchAllResponseModel {
            return ServicePost.doGetEmails(weakReference.get())
        }

        override fun onPostExecute(result: FetchAllResponseModel?) {
            super.onPostExecute(result)
            if(result!!.getEmailList() != null){
                QueryRoomAsyncTask(result.getEmailList()!!, weakReference.get()!!).execute()
            }
            }
        }

    class QueryRoomAsyncTask(private val fetchedList: ArrayList<EmailModel>, context: Context): AsyncTask<Void, Void, EmailModel>(){
        private var weakReference: WeakReference<Context> = WeakReference(context)
        override fun doInBackground(vararg p0: Void?): EmailModel {
            for(item in fetchedList){
                // query database and if it does not exist, add it
                val fetchedList = DatabaseInstance.getInstance(weakReference.get()).recordDao().findBySubject(item.getSubject())
                if(fetchedList.size == 0) {
                    DatabaseInstance.getInstance(weakReference.get()).recordDao()
                        .insertAll(item)
                    Log.i(Constants.QUERY_ROOM_ASYNCTASK, Constants.ADDED_NEW_ITEM)
                }else{
                    Log.i(Constants.QUERY_ROOM_ASYNCTASK, Constants.ITEM_EXISTS)
                }
            }
            return EmailModel(null.toString(), null.toString(), null.toString(), null.toString(), null.toString(), Constants.FALSE)
        }
    }
}