package com.gama.emailreceiver.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.gama.emailreceiver.R
import com.gama.emailreceiver.models.EmailModel
import kotlinx.android.synthetic.main.emails_card.view.*
import android.animation.ObjectAnimator
import android.app.Dialog
import android.os.Build
import com.gama.emailreceiver.helpers.DialogHelper
import com.gama.emailreceiver.helpers.EmailHelper
import kotlinx.android.synthetic.main.dialog_layout.*


class EmailsRecyclerAdapter(private val activity: Activity, private val emailsList: ArrayList<EmailModel>): RecyclerView.Adapter<EmailsRecyclerAdapter.EmailsViewHolder>() {

    private var isExpanded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailsViewHolder {
        return EmailsViewHolder(LayoutInflater.from(activity).inflate(R.layout.emails_card, parent, false))
    }

    override fun getItemCount(): Int {
        return emailsList.size
    }

    override fun onBindViewHolder(holder: EmailsViewHolder, position: Int) {
        holder.email.text = emailsList[position].getEmail()
        holder.date.text = emailsList[position].getdDates().substring(0, 10)
        holder.subject.text = emailsList[position].getSubject()
        holder.body.text = emailsList[position].getBody()
        holder.image.setImageResource(R.drawable.ic_markunread_black_24dp)

        if(holder.body.text.length > 35){
            holder.cv.setOnClickListener {
                if(!isExpanded) {
                    isExpanded = true
                    val animation = ObjectAnimator.ofInt(
                        holder.body,
                        "maxLines",
                        100
                    )
                    animation.duration = 300
                    animation.start()

                    holder.body.text = emailsList[position].getBody().replace("\\n", "\n\n")
                }else{
                    isExpanded = false
                    val animation = ObjectAnimator.ofInt(
                        holder.body,
                        "maxLines",
                        1
                    )
                    animation.duration = 300
                    animation.start()

                    holder.body.text = emailsList[position].getBody().replace("\n\n", "\\n")
                }
            }
        }

        holder.cv.setOnLongClickListener {
            val dialog: Dialog = DialogHelper.getDialogHelper(activity, R.layout.dialog_layout)

            dialog.btn_reply.setOnClickListener {
                EmailHelper.sendMail(activity, emailsList[position].getEmail(),
                emailsList[position].getSubject().replace("\\n", "\n\n"),
                emailsList[position].getBody().replace("\\n", "\n\n"))
            }
            dialog.btn_mark_read.setOnClickListener {  }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.create()
            }
            dialog.show()
            true
        }
    }

    class EmailsViewHolder (view: View) : RecyclerView.ViewHolder(view){
        val cv: CardView = view.cv
        val email: TextView = view.et_email
        val subject: TextView = view.et_subject
        val date: TextView = view.et_date
        val body: TextView = view.et_body
        val image: ImageView = view.imageView
    }
}