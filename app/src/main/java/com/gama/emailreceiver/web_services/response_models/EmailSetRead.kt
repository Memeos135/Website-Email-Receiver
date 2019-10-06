package com.gama.emailreceiver.web_services.response_models

class EmailSetRead(private var message: String) {
    fun getMessage(): String{
        return message
    }

    fun setMessage(message: String){
        this.message = message
    }
}