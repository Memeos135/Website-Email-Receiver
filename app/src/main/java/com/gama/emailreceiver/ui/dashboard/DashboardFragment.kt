package com.gama.emailreceiver.ui.dashboard

import android.app.Activity
import android.app.ProgressDialog
import android.os.AsyncTask
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.gama.emailreceiver.R
import com.gama.emailreceiver.adapters.EmailsRecyclerAdapter
import com.gama.emailreceiver.helpers.ProgressDialogHelper
import com.gama.emailreceiver.utils.Constants
import com.gama.emailreceiver.web_services.ServicePost
import com.gama.emailreceiver.web_services.response_models.FetchAllResponseModel
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.InstanceIdResult
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.lang.ref.WeakReference

class DashboardFragment : Fragment() {

    private lateinit var dashboardViewModel: DashboardViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        if(currentUser == null){
            et_email.visibility = View.VISIBLE
            et_password.visibility = View.VISIBLE
            btn_login.visibility = View.VISIBLE

            btn_login.setOnClickListener { loginMethod(et_email.text.toString(), et_password.text.toString()) }
        }
        // fetch emails - (sync and local storage on hold)
        FetchAllEmailsAsyncTask(activity!!).execute()
    }

    private fun loginMethod(email: String, password: String){
        if(activity != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        disableLoginUI()


                        // get FCM token and store it
                        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener { task: Task<InstanceIdResult> ->
                            if(!task.isSuccessful){
                                Log.d("failed", "FAILED TO RETREIVE TOKEN")
                            }else{
                                Log.d("success", task.result!!.token)
                                // send it to server
                                SendTokenAsyncTask(task.result!!.token, activity!!).execute()
                            }
                        }

                    } else {
                        Snackbar.make(constraint_dash, task.result.toString(), Snackbar.LENGTH_SHORT).show()
                    }
                }
        }
    }


    class SendTokenAsyncTask(private val token: String, activity: Activity): AsyncTask<Void, Void, String>(){
        private var weakReference: WeakReference<Activity> = WeakReference(activity)
        override fun doInBackground(vararg p0: Void?): String {
            val activity = weakReference.get()
            // save to Firebase
            if(!activity!!.isFinishing) {
                return ServicePost.doPostFCMToken(token, activity)
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            val activity = weakReference.get()

            if(result!!.isNotEmpty() && !activity!!.isFinishing) {
                // store/update token
                PreferenceManager.getDefaultSharedPreferences(activity).edit()
                    .putString(Constants.FCM_TOKEN, token).commit()
                Snackbar.make(activity.constraint_dash, "FCM Token has been forwarded", Snackbar.LENGTH_SHORT).show()
            }else{
                Log.d("TokenUpdate:", " Failed to update token.")
            }
        }
    }

    class FetchAllEmailsAsyncTask(activity: Activity): AsyncTask<Void, Void, FetchAllResponseModel>(){
        private var weakReference: WeakReference<Activity> = WeakReference(activity)
        private var progressDialog: ProgressDialog? = null

        override fun onPreExecute() {
            super.onPreExecute()
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                progressDialog = ProgressDialogHelper.getProgressDialog(activity, R.string.fetching_emails)
                progressDialog!!.show()
            }
        }

        override fun doInBackground(vararg p0: Void?): FetchAllResponseModel {
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                return ServicePost.doGetEmails(activity)
            }
            return FetchAllResponseModel(null)
        }

        override fun onPostExecute(result: FetchAllResponseModel?) {
            super.onPostExecute(result)
            val activity = weakReference.get()
            if(!activity!!.isFinishing){
                progressDialog!!.cancel()

                if(result!!.getEmailList() != null){
                    activity.text_dashboard.visibility = View.GONE
                    activity.recyclerView.layoutManager = LinearLayoutManager(activity)
                    activity.recyclerView.adapter = EmailsRecyclerAdapter(activity, result.getEmailList()!!)

                }else{
                    Snackbar.make(activity.constraint_dash, "No emails received", Snackbar.LENGTH_SHORT).show()
                }
            }
        }

    }

    private fun disableLoginUI(){
        et_email.visibility = View.GONE
        et_password.visibility = View.GONE
        btn_login.visibility = View.GONE
    }
}