package com.gama.emailreceiver.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.gama.emailreceiver.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_dashboard.*

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
    }

    private fun loginMethod(email: String, password: String){
        if(activity != null) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity!!) { task ->
                    if (task.isSuccessful) {
                        disableLoginUI()
                    } else {
                        Snackbar.make(constraint_dash, task.result.toString(), Snackbar.LENGTH_SHORT).show()
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