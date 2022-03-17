package com.nearbyvechicleservice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.nearbyvechicleservice.helper.Extra
import com.nearbyvechicleservice.local.LoginType
import com.nearbyvechicleservice.local.PreferenceManager
import com.nearbyvechicleservice.model.Mechanic
import com.nearbyvechicleservice.model.User
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.edtCity
import kotlinx.android.synthetic.main.activity_edit_profile.edtContactNo
import kotlinx.android.synthetic.main.activity_edit_profile.edtEmailAddress
import kotlinx.android.synthetic.main.layout_common_toolbar.view.*

class EditProfileActivity : AppCompatActivity() {
    private var user = User()
    private var mech = Mechanic()
    private lateinit var fm: FirebaseManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        tbEditProfile.tvTitle.text = resources.getString(R.string.edit_profile)
        fm = FirebaseManager(this)
        getIntExtra()
    }

    private fun getIntExtra() {
        intent.let {
            if (intent != null) {
                if (intent.hasExtra(Extra.USER)) {
                    user = intent.getParcelableExtra(Extra.USER) ?: User()
                    setPreData()
                }

                if (intent.hasExtra(Extra.MECHANIC)) {
                    mech = intent.getParcelableExtra(Extra.MECHANIC) ?: Mechanic()
                    setPreData()
                }
            }
        }

        btnUpdate.setOnClickListener {
            updateData()
        }
    }

    private fun setPreData() {
        when (PreferenceManager(this).loginType) {
            LoginType.MECHANIC.type -> {
                edtGaragName.visibility = View.VISIBLE
                edtAddress.visibility = View.VISIBLE
                mech.apply {
                    edtCity.setText(city)
                    edtProfileFullName.setText(fullName)
                    edtEmailAddress.setText(email)
                    edtContactNo.setText(contact)
                    edtGaragName.setText(garagename)
                    edtAddress.setText(address)
                    //edtLocation.setText("")
                }
            }
            else -> {
                edtGaragName.visibility = View.GONE
                edtAddress.visibility = View.GONE
                user.apply {
                    edtCity.setText(city)
                    edtProfileFullName.setText(fullName)
                    edtEmailAddress.setText(email)
                    edtContactNo.setText(contact)
                    //edtLocation.setText("")
                }
            }
        }
    }

    private fun updateData() {
        when (PreferenceManager(this).loginType) {
            LoginType.MECHANIC.type -> {
                mech.id = PreferenceManager(this).userID
                mech.fullName = edtProfileFullName.text.toString().trim()
                mech.city = edtCity.text.toString().trim()
                //mech.address = edtAddress.text.toString().trim()
                mech.email = edtEmailAddress.text.toString().trim()
                mech.contact = edtContactNo.text.toString().trim()
                mech.address = edtAddress.text.toString().trim()
                mech.garagename = edtGaragName.text.toString().trim()
                fm.editUserAndMechData(mech) { isSuccess, message ->
                    if (isSuccess) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        setResult(RESULT_OK)
                        finish()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else -> {
                user.id = PreferenceManager(this).userID
                user.fullName = edtProfileFullName.text.toString().trim()
                user.city = edtCity.text.toString().trim()
                user.email = edtEmailAddress.text.toString().trim()
                user.contact = edtContactNo.text.toString().trim()
                fm.editUserAndMechData(user) { isSuccess, message ->
                    if (isSuccess) {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}