package com.example.fooddonation.Adapter_Dataclasses

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddonation.R

class RiderOrderListAdapter(var orderList: List<rider_order_list>, var context: Context, val onBtn: OnRiderBtnClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class RiderOrderListViewHolder(itemView: View, OnBtn: OnRiderBtnClick): RecyclerView.ViewHolder(itemView)
    {
        val btn: Button = itemView.findViewById(R.id.acceptBtn)
        init {
            btn.setOnClickListener(){onBtn.onButtonClick(adapterPosition, orderList[adapterPosition].food_id)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.rider_order_list_row, parent, false)
        return RiderOrderListViewHolder(view, onBtn)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.row_no21).text=(position+1).toString()+". "
            findViewById<TextView>(R.id.row_sender_address).text = "FROM:  " + orderList[position].donor_add
            findViewById<TextView>(R.id.row_receiver_address).text = "TO:  " + orderList[position].receiver_add
        }
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    interface OnRiderBtnClick
    {
        fun onButtonClick(pos:Int, key:String)
    }
}