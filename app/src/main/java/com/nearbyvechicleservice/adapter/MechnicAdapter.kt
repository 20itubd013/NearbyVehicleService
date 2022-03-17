package com.nearbyvechicleservice.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nearbyvechicleservice.R
import com.nearbyvechicleservice.model.Mechanic
import kotlinx.android.synthetic.main.item_mechanic.view.*

class MechnicAdapter(
    var context: Context,
    private var mechArrayList: ArrayList<Mechanic>,
    private var listener: MechanicalListener
) : RecyclerView.Adapter<MechnicAdapter.MyViewHolder>() {

    interface MechanicalListener {
        fun onItemClick(position: Int)
    }

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val mechName = view.tvMechName
        val garageName = view.tvGarageName
        val address = view.tvAddress
        val phone = view.tvPhone
        val ivPhoe = view.ivPhone


        fun setData() {
            mechName.text = mechArrayList[adapterPosition].fullName
            garageName.text = mechArrayList[adapterPosition].garagename
            address.text = mechArrayList[adapterPosition].address
            phone.text = mechArrayList[adapterPosition].contact
        }
        init {
            phone.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            ivPhoe.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_mechanic, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData()
    }

    override fun getItemCount(): Int = mechArrayList.size

    fun updateData(mechArrayList: ArrayList<Mechanic>) {
        this.mechArrayList = mechArrayList
        notifyDataSetChanged()
    }
}