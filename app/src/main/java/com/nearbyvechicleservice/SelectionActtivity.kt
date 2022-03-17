package com.nearbyvechicleservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.nearbyvechicleservice.local.LoginType
import com.nearbyvechicleservice.local.PreferenceManager
import kotlinx.android.synthetic.main.activity_selection_acttivity.*

class SelectionActtivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selection_acttivity)
        btnSignUp.setOnClickListener {
            if (tblSelection.selectedTabPosition==0){
                startActivity(Intent(this,MechanicSignUpActivity::class.java))
                PreferenceManager(this).loginType = LoginType.MECHANIC.type
            }
            else{
                startActivity(Intent(this,UserSignUpActivity::class.java))
                PreferenceManager(this).loginType = LoginType.USER.type
            }
        }
        btnLogin.setOnClickListener {
            if (tblSelection.selectedTabPosition==0){
                startActivity(Intent(this,LoginActivity::class.java))
                PreferenceManager(this).loginType = LoginType.MECHANIC.type
            }
            else{
                startActivity(Intent(this,LoginActivity::class.java))
                PreferenceManager(this).loginType = LoginType.USER.type
            }
        }
    }
}