package com.example.fooddonation.Adapter_Dataclasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddonation.R

class ReceiverFoodListAdapter(var foodList: List<receiver_food_list>, var context: Context, val onBtn:OnBtnClick) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    inner class ReceiverFoodListViewHolder(itemView: View, onBtn:OnBtnClick): RecyclerView.ViewHolder(itemView)
    {
        val btn:Button = itemView.findViewById(R.id.orderBtn)
        init {
            btn.setOnClickListener(){onBtn.onButtonClick(adapterPosition, foodList[adapterPosition].fid)}
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view= LayoutInflater.from(parent.context).inflate(R.layout.receiver_food_list_row, parent, false)
        return ReceiverFoodListViewHolder(view, onBtn)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.row_no2).text=(position+1).toString()
            findViewById<TextView>(R.id.row_food_name2).text=foodList[position].fname
            findViewById<TextView>(R.id.row_food_type2).text=foodList[position].ftype
        }
    }

    override fun getItemCount(): Int {
        return foodList.size
    }

    interface OnBtnClick
    {
        fun onButtonClick(pos:Int, key:String)
    }

}