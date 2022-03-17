package com.nearbyvechicleservice.local

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Authentic Android Developer on 02-11-2021.
 */
class PreferenceManager(val context: Context) {

    private val pref: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = pref.edit()

    init {
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "NearByVehicle"

        private object Key {
            const val IS_LOGIN = "isLogin"
            const val USER_ID = "userID"
            const val LOGIN_TYPE = "loginType"
            const val FCM_TOKEN = "fcmToken"
        }
    }

    var isLogin: Boolean
        get() = pref.getBoolean(Key.IS_LOGIN, false)
        set(isLogin) {
            editor.putBoolean(Key.IS_LOGIN,isLogin)
            editor.commit()
        }

    var loginType: String
        get() = pref.getString(Key.LOGIN_TYPE,"")?:""
        set(loginType) {
            editor.putString(Key.LOGIN_TYPE,loginType)
            editor.commit()
        }

    var fcmToken: String
        get() = pref.getString(Key.FCM_TOKEN,"")?:""
        set(fcmToken) {
            editor.putString(Key.FCM_TOKEN,fcmToken)
            editor.commit()
        }
    var userID : String
        get() = pref.getString(Key.USER_ID,"")?:""
        set(userID) {
            editor.putString(Key.USER_ID,userID)
            editor.commit()
        }

    fun clearSession() {
        editor.clear()
        editor.commit()
    }


}