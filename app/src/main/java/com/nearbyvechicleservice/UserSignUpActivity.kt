package com.nearbyvechicleservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.nearbyvechicleservice.helper.Helper
import com.nearbyvechicleservice.home.BottomNavigationActivity
import com.nearbyvechicleservice.local.PreferenceManager
import com.nearbyvechicleservice.model.Mechanic
import com.nearbyvechicleservice.model.User
import kotlinx.android.synthetic.main.activity_user_signup.*
import kotlinx.android.synthetic.main.layout_common_toolbar.view.*

class UserSignUpActivity : AppCompatActivity() {

    lateinit var fm : FirebaseManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_signup)
        fm = FirebaseManager(this)
        init()
    }


    private fun init(){
        tbUserSignUp.tvTitle.text = "Sing Up With User"
        tbUserSignUp.ivBack.setOnClickListener{onBackPressed()}
        btnLogin.setOnClickListener {
            if (checkValidation()){
                pb.visibility = View.VISIBLE
                btnLogin.visibility = View.INVISIBLE
                signUp()
            }
        }
    }

    private fun signUp(){
        val user = User()
        user.fullName = edtFullName.text.toString()
        user.city = edtCity.text.toString()
        user.contact = edtContactNo.text.toString()
        user.email = inputEmail.text.toString()
        user.token = PreferenceManager(this).fcmToken

        fm.signUp(user.email.trim(),input_password.text.toString(),user,Mechanic(), true){ isSuccess, message->
            if (isSuccess){
                pb.visibility = View.INVISIBLE
                btnLogin.visibility = View.VISIBLE
                showError(message,true)
                PreferenceManager(this).isLogin = true
                startActivity(Intent(this, BottomNavigationActivity::class.java))
                finishAffinity()
            }
            else{
                pb.visibility = View.INVISIBLE
                btnLogin.visibility = View.VISIBLE
                showError(message)
            }
        }
    }

    private fun checkValidation() : Boolean{
        return when{
            edtFullName.text.toString().isEmpty() -> {
                // Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show()
                showError("Full Name is required")
                return false
            }
            edtContactNo.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Contact no is required")
                return false
            }
            edtContactNo.text.toString().length < 10 ->{
                showError("Contact no must be 10 digits")
                return false
            }
            inputEmail.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Email address is required")
                return false
            }
            input_password.text.toString().isEmpty()->{
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