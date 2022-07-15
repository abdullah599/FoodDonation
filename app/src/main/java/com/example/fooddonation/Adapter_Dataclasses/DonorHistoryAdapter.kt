package com.example.fooddonation.Adapter_Dataclasses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.fooddonation.Adapter_Dataclasses.donor_history_data
import com.example.fooddonation.R


class DonorHistoryAdapter(var todoList: ArrayList<donor_history_data>, var context: Context) :
                          RecyclerView.Adapter<RecyclerView.ViewHolder>(){
	inner class DonorHistoryViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonorHistoryAdapter.DonorHistoryViewHolder {
		val view= LayoutInflater.from(parent.context).inflate(R.layout.donor_history_row, parent, false)
		return DonorHistoryViewHolder(view)
	}
	override fun getItemCount(): Int {
		return todoList.size
	}

	override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
		holder.itemView.apply {
			findViewById<TextView>(R.id.row_no).text=(position+1) as String
			findViewById<TextView>(R.id.row_food_name).text=todoList[position].fname
			findViewById<TextView>(R.id.row_food_type).text=todoList[position].ftype
			findViewById<TextView>(R.id.row_food_status).text=todoList[position].fstatus
		}
	}


}
