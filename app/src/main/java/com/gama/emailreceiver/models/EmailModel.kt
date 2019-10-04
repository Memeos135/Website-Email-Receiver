package com.gama.emailreceiver.models

class EmailModel(private var name: String, private val email: String, private var subject: String, private var body: String, private val dates: String) {
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

    fun getdDates(): String{
        return dates
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
}