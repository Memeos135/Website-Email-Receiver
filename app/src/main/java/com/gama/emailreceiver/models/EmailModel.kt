package com.gama.emailreceiver.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "emails")
class EmailModel(@ColumnInfo(name = "name")private var name: String,
                 @ColumnInfo(name = "email")private var email: String,
                 @ColumnInfo(name = "subject")private var subject: String,
                 @ColumnInfo(name = "body")private var body: String,
                 @ColumnInfo(name = "dates")private var dates: String) {
    @PrimaryKey(autoGenerate = true)
    private var uid: Int = 0

    private var isRead = false

    fun getIsRead(): Boolean{
        return isRead
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

    fun setIsRead(isRead: Boolean){
        this.isRead = isRead
    }
}