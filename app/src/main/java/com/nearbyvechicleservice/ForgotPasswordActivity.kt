package com.nearbyvechicleservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.EmailAuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.nearbyvechicleservice.helper.Helper
import kotlinx.android.synthetic.main.activity_forgotpassword.*
import kotlinx.android.synthetic.main.layout_common_toolbar.view.*

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var fm  : FirebaseManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword)
        fm = FirebaseManager(this)
        init()
    }

   private fun init() {
       tbForgotPassword.tvTitle.text = "Forgot Password"
       tbForgotPassword.ivBack.setOnClickListener { onBackPressed() }
       btnForgotPassword.setOnClickListener {
           if (checkValidation()){
               forgotPassword()
           }
       }
   }


    private fun forgotPassword(){
        ForgotPaaswordpb.visibility = View.VISIBLE

        btnForgotPassword.setOnClickListener {
            fm.forgotPassword(edtForgotPassword.text.toString()){ isSuccess,message ->
                if (isSuccess){
                    btnForgotPassword.visibility = View.INVISIBLE
                    showError(message,true)
                }
                else {
                    btnForgotPassword.visibility = View.INVISIBLE
                    showError(message)

                }
            }
        }
    }

    private fun checkValidation(): Boolean {
        return when {
            edtForgotPassword.text.toString().isEmpty() -> {
                showError("Enter Email Address")
                return false
            }
            else -> {
                true
            }

        }
    }

    private fun showError(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customsnackbar(clRoot, message, this, isSuccess)
        }
    }

}