package com.nearbyvechicleservice.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nearbyvechicleservice.FirebaseManager
import com.nearbyvechicleservice.R
import com.nearbyvechicleservice.adapter.MechnicAdapter
import com.nearbyvechicleservice.helper.Helper
import com.nearbyvechicleservice.model.Mechanic
import kotlinx.android.synthetic.main.fragment_garage.*

class GarageFragment : Fragment(), MechnicAdapter.MechanicalListener {


    lateinit var mechAdapter: MechnicAdapter
    private var mechArrayList: ArrayList<Mechanic> = arrayListOf()
    private lateinit var firebaseManager: FirebaseManager
    var mobileNo : String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_garage, container, false)
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvData.apply {
            layoutManager = LinearLayoutManager(activity)
            mechAdapter = MechnicAdapter(requireActivity(),arrayListOf(),this@GarageFragment)
            adapter = mechAdapter
        }
        init()
        getMechList()
    }

    private fun init() {
        firebaseManager = FirebaseManager(requireContext())
        //  t.tvTitle.text = resources.getString(R.string.grages)
    }

    private fun getMechList() {
        pbMechanicList.visibility = View.VISIBLE
        firebaseManager.getMechanicData { isSuccess, message, mechList ->
            if (isSuccess) {
                pbMechanicList.visibility = View.GONE
                rvData.visibility = View.VISIBLE
                mechArrayList = mechList
                mechAdapter.updateData(mechArrayList)
            } else {
                pbMechanicList.visibility = View.GONE
                showError(message)
            }
        }
    }

    override fun onItemClick(position: Int) {
        mobileNo = mechArrayList[position].contact
        if (checkPermission()){
            addCalling()
        }
        else{
            ActivityCompat.requestPermissions(requireActivity(),
                arrayOf(android.Manifest.permission.CALL_PHONE),
               1)
        }
    }

    private fun addCalling() {
        val callIntent = Intent(Intent.ACTION_CALL)
        callIntent.data = Uri.parse("tel:$mobileNo")
        startActivity(callIntent)
    }

    private fun checkPermission(): Boolean {
        return (checkSelfPermission(requireContext(),
            android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            1 -> {
                if (grantResults.size == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    addCalling()
                }
            }
        }
    }
    private fun showError(message: String, isSuccess: Boolean = false) {
        if (message.isNotEmpty()) {
            Helper.customsnackbar(clRoot, message, requireContext(), isSuccess)
        }
    }



}