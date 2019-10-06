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
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Build
import android.util.Log
import com.gama.emailreceiver.helpers.DialogHelper
import com.gama.emailreceiver.helpers.EmailHelper
import com.gama.emailreceiver.helpers.ProgressDialogHelper
import com.gama.emailreceiver.room.DatabaseInstance
import com.gama.emailreceiver.utils.Constants
import com.gama.emailreceiver.web_services.ServicePost
import com.gama.emailreceiver.web_services.response_models.EmailSetRead
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_layout.*
import kotlinx.android.synthetic.main.emails_card.*
import java.lang.ref.WeakReference
import kotlin.collections.ArrayList


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
        // date always comes in a specific format, so instead sdf just substring
        holder.date.text = emailsList[position].getDates().substring(0, 10)
        holder.subject.text = emailsList[position].getSubject()
        holder.body.text = emailsList[position].getBody()
        holder.image.visibility = View.VISIBLE

        if(emailsList[position].getStatusRead() == Constants.TRUE){
            holder.image.setImageResource(R.drawable.ic_check_box_green_24dp)
        }else{
            holder.image.setImageResource(R.drawable.ic_markunread_red_24dp)
        }

        // since this app will only be used by myself, I am using this condition
        if(holder.body.text.length > 35){
            holder.cv.setOnClickListener {
                if(!isExpanded) {
                    isExpanded = true
                    val animation = ObjectAnimator.ofInt(
                        holder.body,
                        Constants.MAXLINES,
                        100
                    )
                    animation.duration = 300
                    animation.start()

                    holder.body.text = emailsList[position].getBody().replace("\\n", "\n\n")
                }else{
                    isExpanded = false
                    val animation = ObjectAnimator.ofInt(
                        holder.body,
                        Constants.MAXLINES,
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
            dialog.btn_mark_read.setOnClickListener {
                SetEmailReadAsyncTask(emailsList[position], activity, dialog, holder).execute()
            }

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

    class SetEmailReadAsyncTask(private val emailModel: EmailModel, activity: Activity, private val dialog: Dialog, private val holder: EmailsViewHolder): AsyncTask<Void, Void, EmailSetRead>(){
        private var weakReference: WeakReference<Activity> = WeakReference(activity)
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                progressDialog = ProgressDialogHelper.getProgressDialog(activity, R.string.updating)
                progressDialog!!.show()
            }
        }

        override fun doInBackground(vararg p0: Void?): EmailSetRead {
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                return ServicePost.doPostEmail(activity, emailModel)
            }
            return EmailSetRead("")
        }

        override fun onPostExecute(result: EmailSetRead?) {
            super.onPostExecute(result)
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                progressDialog!!.cancel()
                // if remote update is successful, update local
                if(result!!.getMessage().isNotEmpty() && result.getMessage().contains(Constants.SUCCESSFUL)){
                    Snackbar.make(activity.cv, R.string.status_update_successful, Snackbar.LENGTH_SHORT).show()
                    emailModel.setStatusRead(Constants.TRUE)

                    UpdateLocalStatusRead(emailModel, activity).execute()
                    dialog.cancel()
                    holder.image.setImageResource(R.drawable.ic_check_box_green_24dp)
                }else{
                    Snackbar.make(activity.cv, R.string.status_update_failed, Snackbar.LENGTH_SHORT).show()
                }
            }
        }

        class UpdateLocalStatusRead(private var emailModel: EmailModel, activity: Activity): AsyncTask<Void, Void, Void>(){
            private var weakReference: WeakReference<Activity> = WeakReference(activity)
            override fun doInBackground(vararg p0: Void?): Void? {
                DatabaseInstance.getInstance(weakReference.get()).recordDao().update(Constants.TRUE, emailModel.getDates(), emailModel.getSubject())
                return null
            }

        }
    }
}