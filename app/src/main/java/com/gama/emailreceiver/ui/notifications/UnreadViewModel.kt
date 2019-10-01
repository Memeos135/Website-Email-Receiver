package com.gama.emailreceiver.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class UnreadViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is unread Fragment"
    }
    val text: LiveData<String> = _text
}