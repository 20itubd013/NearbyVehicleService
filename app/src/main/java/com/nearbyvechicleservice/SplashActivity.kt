package com.nearbyvechicleservice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nearbyvechicleservice.home.BottomNavigationActivity
import com.nearbyvechicleservice.local.PreferenceManager

class SplashActivity : AppCompatActivity() {
    private val splashTime = 2000L

    val baseHandler = Handler(Looper.getMainLooper())
    lateinit var preferenceManager : PreferenceManager
    var runnable = Runnable { /*Set runnable and use its handler in entire project*/ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        preferenceManager = PreferenceManager(this)

        runnable = Runnable {
            if (!preferenceManager.isLogin){
                startActivity(Intent(this,SelectionActtivity::class.java))
                finishAffinity()
            }else{
                startActivity(Intent(this,BottomNavigationActivity::class.java))
                finishAffinity()
            }

        }
        init()
    }

    private fun init() {
        baseHandler.postDelayed(runnable, splashTime)
    }

    override fun onDestroy() {
        super.onDestroy()
        baseHandler.removeCallbacks(runnable)
    }
}