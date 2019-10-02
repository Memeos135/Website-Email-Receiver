package com.gama.emailreceiver.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gama.emailreceiver.R
import com.gama.emailreceiver.models.EmailModel
import kotlinx.android.synthetic.main.emails_card.view.*

class EmailsRecyclerAdapter(private val activity: Activity, private val emailsList: ArrayList<EmailModel>): RecyclerView.Adapter<EmailsRecyclerAdapter.EmailsViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailsViewHolder {
        return EmailsViewHolder(LayoutInflater.from(activity).inflate(R.layout.emails_card, parent, false))
    }

    override fun getItemCount(): Int {
        return emailsList.size
    }

    override fun onBindViewHolder(holder: EmailsViewHolder, position: Int) {
        holder.temp.text = emailsList[position].getEmail()
    }

    class EmailsViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val temp: TextView = view.textView
    }
}