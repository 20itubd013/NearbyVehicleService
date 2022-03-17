package com.nearbyvechicleservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nearbyvechicleservice.helper.Helper
import com.nearbyvechicleservice.home.BottomNavigationActivity
import com.nearbyvechicleservice.local.PreferenceManager
import com.nearbyvechicleservice.model.Mechanic
import com.nearbyvechicleservice.model.User
import kotlinx.android.synthetic.main.activity_mechanic.*
import kotlinx.android.synthetic.main.layout_common_toolbar.view.*

class MechanicSignUpActivity : AppCompatActivity() {
    lateinit var fm : FirebaseManager

    private var lat :String = ""
    private var lng :String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic)
        fm = FirebaseManager(this)
        init()
        addListener()
    }

    private fun init(){
        tbMech.tvTitle.text = "Sing Up With Mechanic"
        tbMech.ivBack.setOnClickListener{onBackPressed()}
        btnLogin.setOnClickListener {
           if (checkValidation()){
               signUp()
           }
        }
    }

    private fun addListener(){
        edtgaragePlace.setOnClickListener {
            startActivityForResult(Intent(this
            ,MapsActivity::class.java),1)
        }
    }

    private fun signUp(){
        val mech = Mechanic()
        mech.fullName = edtFullName.text.toString().trim()
        mech.city = edtCity.text.toString().trim()
        mech.contact = edtContactNo.text.toString().trim()
        mech.address = edtAddress.text.toString().trim()
        mech.garagename = edtGarageName.text.toString().trim()
        mech.email = edtEmailAddress.text.toString().trim()
        mech.lat = lat
        mech.lng= lng
        mech.token = PreferenceManager(this).fcmToken
        pb.visibility = View.VISIBLE
        btnLogin.visibility = View.INVISIBLE

        fm.signUp(mech.email,edtPassword.text.toString(), User(),
            mech, false){ isSuccess, message->
            if (isSuccess){
                pb.visibility = View.INVISIBLE
                btnLogin.visibility = View.VISIBLE
                showError(message,true)
                PreferenceManager(this).isLogin = true
                startActivity(Intent(this,BottomNavigationActivity::class.java))
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
            edtGarageName.text.toString().isEmpty() -> {
                // Toast.makeText(this, "Email address is required", Toast.LENGTH_SHORT).show()
                showError("Garage Name is required")
                return false
            }
            edtContactNo.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Contact no is required")
                return false
            }
            edtContactNo.text.toString().length < 10 ->{
                showError("Contact no must have 10 digits")
                return false
            }

            edtCity.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("City is required")
                return false
            }
            edtEmailAddress.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Email address is required")
                return false
            }
            edtPassword.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Password is required")
                return false
            }
            edtAddress.text.toString().isEmpty()->{
                //Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
                showError("Address is required")
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== RESULT_OK){
            when(requestCode){
                1 ->{
                    if (data!=null){
                         lat = data.getStringExtra("lat").toString()
                         lng = data.getStringExtra("lng").toString()

                        edtgaragePlace.setText(lat+","+lng)
                    }
                }
            }
        }
    }
}