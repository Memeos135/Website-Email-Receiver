package com.gama.emailreceiver.models

class EmailModel(private val name: String, private val email: String, private val subject: String, private  val body: String, private val dates: String) {
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
}