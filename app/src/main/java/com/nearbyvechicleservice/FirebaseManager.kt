package com.nearbyvechicleservice

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.nearbyvechicleservice.helper.Helper
import com.nearbyvechicleservice.local.LoginType
import com.nearbyvechicleservice.local.PreferenceManager
import com.nearbyvechicleservice.model.Mechanic
import com.nearbyvechicleservice.model.User


class FirebaseManager(val context: Context) {


    //Log tag
    private val tag = this::class.java.simpleName

    //Authentication
    private val auth = Firebase.auth
    val pref = PreferenceManager(context)

    //Firestore
    private val firestore = Firebase.firestore

    private val function = Firebase.functions

    //FireBase Storage
    // private val storage = FirebaseStorage.getInstance(BuildConfig.storageBucket).reference

    //Authentication--------------------------------------------------------------------------------
    /**
     * Check whether user is already login or not
     */
    val isLoggedIn: Boolean get() = auth.currentUser != null

    /**
     * Collection which can be by-passed without authentication
     */
    // private val byPassedCollection = hashSetOf(FSCollection.FSIssues.key)

    /**
     * Get current logged-in user
     */
    var currentUser = auth.currentUser

    /**
     * Sign in / Login in used with email & password
     * @param email : Email Address
     * @param password : Password
     * @param callback : Return whether login is successful or not
     */
    fun login(email: String, password: String, callback: (Boolean, String) -> Unit) {
        Log.w("", "$tag : $email : $password")
        if (Helper.isNetworkAvailable(context)) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Sign in success
                        Log.w("", "$tag : Login success")
                        currentUser = auth.currentUser
                        Log.d("id", currentUser.uid)
                        pref.userID = currentUser.uid
                        //send callback
                        //UserRepository().setUserId(currentUser?.uid.makeString)
                        callback.invoke(true, task.exception?.localizedMessage ?: "")
                    } else {
                        //Sign in failure
                        Log.w("", "$tag : Login failure")
                        //send callback
                        callback.invoke(false, task.exception?.localizedMessage ?: "")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("", "$tag : Error Login $exception")
                    //send callback
                    callback.invoke(false, exception.localizedMessage ?: "")
                }
        } else {
            callback.invoke(false, context.resources.getString(R.string.err_network))
        }
        //callback.invoke(true, "Lo")
    }

    /**
     * Sign up user with email and password
     * @param email : Email Address
     * @param password : Password
     * @param user : [User]
     * @param callback : Return whether login is successful or not
     */
    fun signUp(
        email: String,
        password: String,
        user: User,
        mechanic: Mechanic,
        isUser: Boolean,
        callback: (Boolean, String) -> Unit
    ) {
        Log.w("", "$tag : $email : $password")

        if (Helper.isNetworkAvailable(context)) {

            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        //Sign up success
                        Log.w("", "$tag : Sign up success")
                        currentUser = auth.currentUser
                        pref.userID = currentUser.uid
                        // UserRepository().setUserId(currentUser?.uid.makeString)
                        //send User Data
                        if (isUser) {
                            user.sendData {
                                callback.invoke(true, task.exception?.localizedMessage ?: "")
                            }
                        } else {
                            mechanic.setData {
                                callback.invoke(true, task.exception?.localizedMessage ?: "")
                            }
                        }
                    } else {
                        //Sign up failure
                        Log.w("", "$tag : Sign up failure")
                        //send callback
                        callback.invoke(false, task.exception?.localizedMessage ?: "")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("", "$tag : Error sign up $exception")
                    //send callback
                    callback.invoke(false, exception.localizedMessage ?: "")
                }
        } else {
            callback.invoke(false, context.resources.getString(R.string.err_network))
        }

    }

    private fun User?.sendData(send: (Boolean) -> Unit) {
        //set uid of the current user
        this!!.id = currentUser?.uid ?: ""
        if (id?.isNotEmpty() == true) {

            firestore.collection("Client")
                .document(this.id!!)
                .set(this, SetOptions.merge())
                .addOnCompleteListener { task ->
                    send.invoke(task.isSuccessful)
                }
                .addOnFailureListener { exception ->
                    Log.w("", "$tag : Error getting documents. $exception")
                    send.invoke(false)
                }
        } else {
            send.invoke(false)
        }
    }

    private fun Mechanic?.setData(send: (Boolean) -> Unit) {
        //set uid of the current user
        this!!.id = currentUser?.uid ?: ""
        if (id?.isNotEmpty() == true) {

            firestore.collection("Mechanic")
                .document(this.id!!)
                .set(this, SetOptions.merge())
                .addOnCompleteListener { task ->
                    send.invoke(task.isSuccessful)
                }
                .addOnFailureListener { exception ->
                    Log.w("", "$tag : Error getting documents. $exception")
                    send.invoke(false)
                }
        } else {
            send.invoke(false)
        }
    }

    fun forgotPassword(email: String, listener: (Boolean, String) -> Unit) {

        if (Helper.isNetworkAvailable(context)) {

            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        listener.invoke(true, task.exception?.localizedMessage ?: "")
                    } else {
                        listener.invoke(false, task.exception?.localizedMessage ?: "")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.w("", "$tag : Error sign up $exception")
                    //send callback
                    listener.invoke(false, exception.localizedMessage ?: "")
                }
        } else {
            listener.invoke(false, context.resources.getString(R.string.err_network))
        }


    }

    // get Mechanic data
    fun getMechanicData(listener: (Boolean, String, ArrayList<Mechanic>) -> Unit) {
        if (Helper.isNetworkAvailable(context)) {

            firestore.collection("Mechanic")
                .get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        val mechList: ArrayList<Mechanic> = arrayListOf()
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")
                            mechList.add(
                                Mechanic().apply {
                                    fullName = document.data["fullName"].toString()
                                    contact = document.data["contact"].toString()
                                    city = document.data["city"].toString()
                                    address = document.data["address"].toString()
                                    lat = document.data["lat"].toString()
                                    lng = document.data["lng"].toString()
                                    garagename = document.data["garagename"].toString()
                                    email = document.data["email"].toString()
                                }
                            )
                        }
                        listener.invoke(true, "", mechList)
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.invoke(false, exception.message ?: "", arrayListOf())
                }
        } else {
            listener.invoke(false, context.resources.getString(R.string.err_network), arrayListOf())
        }
    }

    // get user data
    fun getSingleUserData(listener: (Boolean,String,Any) -> Unit) {
        val user = User()
        val mech = Mechanic()

        if (Helper.isNetworkAvailable(context)) {

            firestore.collection(if (pref.loginType == LoginType.MECHANIC.type) "Mechanic" else "Client")
                .whereEqualTo("id", pref.userID)
                .get()
                .addOnSuccessListener { result ->
                    if (result != null) {
                        for (document in result) {
                            Log.d(TAG, "${document.id} => ${document.data}")

                            if (pref.loginType == LoginType.MECHANIC.type) {
                                mech.apply {
                                    fullName = document.data["fullName"].toString()
                                    contact = document.data.get("contact").toString()
                                    city = document.data.get("city").toString()
                                    email = document.data.get("email").toString()
                                    address = document.data.get("address").toString()
                                    garagename = document.data.get("garagename").toString()
                                }
                                listener.invoke(true, "", mech)
                            } else {
                                user.apply {
                                    fullName = document.data["fullName"].toString()
                                    contact = document.data.get("contact").toString()
                                    city = document.data.get("city").toString()
                                    email = document.data.get("email").toString()
                                }
                                listener.invoke(true, "", user)
                            }
                        }
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.invoke(false, exception.message ?: "", user)
                }
        } else {
            listener.invoke(false, context.resources.getString(R.string.err_network), user)
        }
    }


    /**
     * Edit User And Mechanic Data
     */

    fun editUserAndMechData(data: Any, listener: (Boolean, String) -> Unit) {
        if (Helper.isNetworkAvailable(context)) {
            firestore.collection(if (pref.loginType == LoginType.MECHANIC.type) "Mechanic" else "Client")
                .document(pref.userID)
                .set(if (data is Mechanic) data else if (data is User) data else data as User)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful){
                        listener.invoke(true,"Data Updated Successfully")
                    }
                }
                .addOnFailureListener { exception ->
                    Log.d(TAG, "Error getting documents: ", exception)
                    listener.invoke(false, exception.message ?: "")
                }
        } else {
            listener.invoke(false, context.resources.getString(R.string.err_network))
        }
    }
}