package com.gama.emailreceiver.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson

@Entity(tableName = "emails")
class EmailModel(@ColumnInfo(name = "name")private var name: String,
                 @ColumnInfo(name = "email")private var email: String,
                 @ColumnInfo(name = "subject")private var subject: String,
                 @ColumnInfo(name = "body")private var body: String,
                 @ColumnInfo(name = "dates")private var dates: String,
                 @ColumnInfo(name = "statusRead")private var statusRead: String) {
    @PrimaryKey(autoGenerate = true)
    private var uid: Int = 0

    fun toJSON(): String{
        return Gson().toJson(this)
    }

    fun getStatusRead(): String{
        return statusRead
    }

    fun getName(): String{
        return name
    }

    fun getEmail(): String{
        return email
    }

    fun getSubject(): String{
        return subject
    }

    fun getBody(): String{
        return body
    }

    fun getDates(): String{
        return dates
    }

    fun getUid(): Int{
        return uid
    }

    fun setUid(newUid: Int){
        uid = newUid
    }

    fun setName(newName: String){
        name = newName
    }

    fun setSubject(newSubject: String){
        subject = newSubject
    }

    fun setBody(newBody: String){
        body = newBody
    }

    fun setEmail(newEmail: String){
        email = newEmail
    }

    fun setDates(newDate: String){
        dates = newDate
    }

    fun setStatusRead(statusRead: String){
        this.statusRead = statusRead
    }
}