package com.gama.emailreceiver.web_services

import android.app.Activity
import android.content.Context
import android.util.Log
import com.gama.emailreceiver.R
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
                Log.d("Error", e.printStackTrace().toString())
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
                Log.d("Error", e.printStackTrace().toString())
                return FetchAllResponseModel(null)
            }
        }
    }
}