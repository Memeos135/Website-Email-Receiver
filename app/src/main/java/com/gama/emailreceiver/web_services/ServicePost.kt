package com.gama.emailreceiver.web_services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.gama.emailreceiver.R
import com.gama.emailreceiver.models.EmailModel
import com.gama.emailreceiver.utils.Constants
import com.gama.emailreceiver.web_services.response_models.EmailSetRead
import com.gama.emailreceiver.web_services.response_models.FetchAllResponseModel
import com.google.gson.Gson
import okhttp3.*
import java.lang.Exception
import java.util.concurrent.TimeUnit

class ServicePost {
    companion object{
        fun doPostFCMToken(token: String, activity: Activity?): String{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val body: RequestBody = RequestBody.create(
                    MediaType.parse("x-www-form-urlencoded"),
                    "{\"token\": \"$token\"}"
                )
                val requestBuilder: Request.Builder = Request.Builder()
                    .url(activity!!.getString(R.string.token_url))
                    .post(body)

                val response: Response = client.newCall(requestBuilder.build()).execute()

                return response.body()!!.string()
            }catch (e: Exception){
                Log.d("Error", e.printStackTrace().toString())
                return ""
            }
        }

        fun doGetEmailsUnread(activity: Activity?): FetchAllResponseModel{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val requestBuilder: Request.Builder = Request.Builder()
                    .url(activity!!.getString(R.string.emails_unread_url))
                    .get()

                val response: Response = client.newCall(requestBuilder.build()).execute()
                var result = response.body()!!.string()
                result = result.substring(0, result.length-1) + "]}"

                return Gson().fromJson(result, FetchAllResponseModel::class.java)
            }catch (e: Exception){
                Log.d(Constants.ERROR, e.printStackTrace().toString())
                return FetchAllResponseModel(null)
            }
        }

        fun doGetEmails(activity: Activity?): FetchAllResponseModel{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val requestBuilder: Request.Builder = Request.Builder()
                    .url(activity!!.getString(R.string.emails_url))
                    .get()

                val response: Response = client.newCall(requestBuilder.build()).execute()
                var result = response.body()!!.string()
                result = result.substring(0, result.length-1) + "]}"

                return Gson().fromJson(result, FetchAllResponseModel::class.java)
            }catch (e: Exception){
                Log.d(Constants.ERROR, e.printStackTrace().toString())
                return FetchAllResponseModel(null)
            }
        }

        fun doGetEmails(context: Context?): FetchAllResponseModel{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val requestBuilder: Request.Builder = Request.Builder()
                    .url(context!!.getString(R.string.emails_url))
                    .get()

                val response: Response = client.newCall(requestBuilder.build()).execute()
                var result = response.body()!!.string()
                result = result.substring(0, result.length-1) + "]}"

                return Gson().fromJson(result, FetchAllResponseModel::class.java)
            }catch (e: Exception){
                Log.d(Constants.ERROR, e.printStackTrace().toString())
                return FetchAllResponseModel(null)
            }
        }

        fun doPostEmail(activity: Activity, emailModel: EmailModel): EmailSetRead{
            try {
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(15, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .build()

                val body: RequestBody = RequestBody.create(
                    MediaType.parse("x-www-form-urlencoded"),
                    emailModel.toJSON()
                )
                val requestBuilder: Request.Builder = Request.Builder()
                    .url(activity.getString(R.string.email_write_url))
                    .post(body)

                val response: Response = client.newCall(requestBuilder.build()).execute()
                val result = response.body()!!.string()

                return Gson().fromJson(result, EmailSetRead::class.java)
            }catch (e: Exception){
                Log.d(Constants.ERROR, e.printStackTrace().toString())
                return EmailSetRead("")
            }
        }
    }
}