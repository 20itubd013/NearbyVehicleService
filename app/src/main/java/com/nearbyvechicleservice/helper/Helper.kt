package com.nearbyvechicleservice.helper

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.nearbyvechicleservice.R

object Helper {

    fun customsnackbar(view: View, message: String, context: Context, isSuccess: Boolean? = null){
        val snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)

        val tvSnackbar = snackbar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
        tvSnackbar.maxLines = 3

        tvSnackbar.setTextColor(ContextCompat.getColor(context, R.color.white))
        snackbar.setBackgroundTint(
            ContextCompat.getColor(
                context,
                when (isSuccess){
                    true -> R.color.snackBar_green
                    false -> R.color.snackbar_red
                    else -> R.color.black
                }
            )
        )
        snackbar.show()

    }




    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val capabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            if (capabilities != null) {
                when {
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                        return true
                    }
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                        return true
                    }
                }
            }
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                return true
            }
        }

        return false
    }


}