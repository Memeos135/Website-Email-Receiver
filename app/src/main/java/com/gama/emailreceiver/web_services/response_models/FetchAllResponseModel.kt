package com.gama.emailreceiver.web_services.response_models

import com.gama.emailreceiver.models.EmailModel

class FetchAllResponseModel(private val emailList: ArrayList<EmailModel>?) {
    fun getEmailList(): ArrayList<EmailModel>? {
        return emailList
    }
}