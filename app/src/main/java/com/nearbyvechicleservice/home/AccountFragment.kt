package com.nearbyvechicleservice.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.nearbyvechicleservice.EditProfileActivity
import com.nearbyvechicleservice.FirebaseManager
import com.nearbyvechicleservice.R
import com.nearbyvechicleservice.SelectionActtivity
import com.nearbyvechicleservice.helper.Extra
import com.nearbyvechicleservice.local.LoginType
import com.nearbyvechicleservice.local.PreferenceManager
import com.nearbyvechicleservice.model.Mechanic
import com.nearbyvechicleservice.model.User
import kotlinx.android.synthetic.main.activity_mechanic.tvEmail
import kotlinx.android.synthetic.main.activity_mechanic.tvFullName
import kotlinx.android.synthetic.main.fragment_account.*

class AccountFragment : Fragment(), View.OnClickListener {

    private lateinit var fm: FirebaseManager
    private var user = User()
    private var mech = Mechanic()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root = inflater.inflate(R.layout.fragment_account, container, false)
        fm = FirebaseManager(requireContext())
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getSingleUserData()
        addListener()
    }

    private fun addListener() {
        ivEditProfile.setOnClickListener(this)
        tvEditProfile.setOnClickListener(this)
        ivLogOut.setOnClickListener(this)
        tvLogOut.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v) {
            ivEditProfile, tvEditProfile -> {
                if (PreferenceManager(requireContext()).loginType == LoginType.MECHANIC.type) {
                    startActivityForResult(
                        Intent(requireContext(), EditProfileActivity::class.java).putExtra(
                            Extra.MECHANIC, mech
                        ), 1
                    )
                } else {
                    startActivityForResult(
                        Intent(requireContext(), EditProfileActivity::class.java).putExtra(
                            Extra.USER, user
                        ), 1
                    )
                }
            }
            ivLogOut, tvLogOut -> {
                PreferenceManager(requireContext()).clearSession()
                startActivity(Intent(requireContext(), SelectionActtivity::class.java))
                activity?.finishAffinity()
            }
        }
    }

    private fun getSingleUserData() {
        fm.getSingleUserData { isSucces, message, resulType ->
            if (isSucces) {
                when (resulType) {
                    is User -> {
                        this.user = resulType
                        tvName.text = user.fullName
                        tvFullName.text = user.fullName
                        tvEmail.text = user.email
                    }
                    is Mechanic -> {
                        this.mech = resulType
                        tvName.text = mech.fullName[0].toString()
                        tvFullName.text = mech.fullName
                        tvEmail.text = mech.email
                    }
                }
            } else {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == AppCompatActivity.RESULT_OK) {
            when (requestCode) {
                1 -> {
                    getSingleUserData()
                }
            }
        }
    }
}