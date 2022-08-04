package com.example.fooddonation.ui.History

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.DonorHistoryAdapter
import com.example.fooddonation.Adapter_Dataclasses.donor_history_data
import com.example.fooddonation.Auth
import com.example.fooddonation.databinding.FragmentReceiverHistoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class ReceiverHistoryFragment : Fragment() {






	private var _binding: FragmentReceiverHistoryBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


		_binding = FragmentReceiverHistoryBinding.inflate(inflater, container, false)
		val root: View = binding.root



		val foodList: ArrayList<donor_history_data> = ArrayList()
		/** Database call **/
		val database = Firebase.database
		val myRef = database.getReference("Food")
		myRef.addValueEventListener(object: ValueEventListener{

			override fun onDataChange(snapshot: DataSnapshot) {
				for(food in snapshot.children){
					if(food.child("receiver_id").value==Auth.currentUser?.uid){
						var stat="null"
						if(food.child("status").value.toString()=="Donated")
							stat="Received"
						else
							stat="Not Received"
						foodList.add(donor_history_data(food.child("name").value.toString(), food.child("type").value.toString(), stat))
					}
				}
				(binding.RrvHistory.adapter as DonorHistoryAdapter).notifyDataSetChanged()


			}

			override fun onCancelled(error: DatabaseError) {
				Log.w("error", "Failed to read value.", error.toException())
			}

		})


		var adapter= DonorHistoryAdapter(foodList, this.requireContext())
		binding.RrvHistory.adapter=adapter
		binding.RrvHistory.layoutManager = LinearLayoutManager(this.requireContext())

		return root
	}


	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}