package com.gama.emailreceiver.helpers

import android.app.Activity
import android.content.Intent
import android.net.Uri

class EmailHelper {
    companion object{
        fun sendMail(activity: Activity, email: String, subject: String, body: String) {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO,
                Uri.parse("mailto:$email")
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Re: $subject")
            emailIntent.putExtra(Intent.EXTRA_TEXT,
                "In regard to the sent email with the following content:\n\n $body\n\n\n________________________________________\n\n\n")
            activity.startActivity(Intent.createChooser(emailIntent, "Send Mail"))
        }
    }
}