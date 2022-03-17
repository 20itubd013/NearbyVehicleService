package com.nearbyvechicleservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nearbyvechicleservice.helper.Helper
import com.nearbyvechicleservice.home.BottomNavigationActivity
import com.nearbyvechicleservice.local.PreferenceManager
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.btnLogin
import kotlinx.android.synthetic.main.activity_login.inputEmail

class LoginActivity : AppCompatActivity() {
    private lateinit var fm: FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        fm = FirebaseManager(this)
        init()
    }

    private fun init() {

        forgotpassword.setOnClickListener {
            startActivity(Intent(this,ForgotPasswordActivity::class.java))
        }

        btnLogin.setOnClickListener {
           if (checkValidation()){
               pbLogin.visibility = View.VISIBLE
               btnLogin.visibility = View.INVISIBLE

               fm.login(inputEmail.text.toString().trim(), inputpassword.text.toString().trim()) { isSuccess, message ->
                   if (isSuccess) {
                       startActivity(Intent(this,BottomNavigationActivity::class.java))
                       finishAffinity()
                       PreferenceManager(this).isLogin = true
                       pbLogin.visibility = View.INVISIBLE
                       btnLogin.visibility = View.VISIBLE

                   } else {
                       //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        showError(message)
                       pbLogin.visibility = View.INVISIBLE
                       btnLogin.visibility = View.VISIBLE
                   }
               }
           }
        }
    }

    private fun checkValidation() : Boolean{
        return when{
            inputEmail.text.toString().isEmpty() -> {
                // Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show()
                showError("Email address is required")
                return false
            }
            inputpassword.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Password is required")
                return false
            }
            else -> {
                true
            }
        }
    }



    private fun showError(message: String, isSuccess: Boolean = false){
        if (message.isNotEmpty()){
            Helper.customsnackbar(clRoot, message, this, isSuccess)
        }
    }
}