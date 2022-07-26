package com.example.fooddonation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.ReceiverFoodListAdapter
import com.example.fooddonation.Adapter_Dataclasses.receiver_food_list
import com.example.fooddonation.databinding.FragmentReceiverHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ReceiverHomeFragment : Fragment() {

	private var _binding: FragmentReceiverHomeBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

		_binding = FragmentReceiverHomeBinding.inflate(inflater, container, false)
		val root: View = binding.root

		/** Food List and adapter for recycler view **/
		val foodList: ArrayList<receiver_food_list> = ArrayList()
		val adapter = ReceiverFoodListAdapter(foodList,this.requireContext())

		// initializing auth
		val auth = Firebase.auth

		/** Database call **/
		val database = Firebase.database
		val ref = auth.currentUser?.let { database.getReference("Users").child("Receiver").child(it.uid) }

		var receiverCity:String = ""

		// Getting the city of the current user - receiver
		ref?.addValueEventListener(object: ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				receiverCity = snapshot.child("city").value.toString()
			}
			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
			}
		})

		// All food donated in the same city
		val ref2 = database.getReference("Food")
		ref2.addValueEventListener(object: ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				for (food in snapshot.children)
				{
					// Getting donor's city
					val tempRef = database.getReference("Users").child("Donor").child(food.child("donor_id").value.toString())
					tempRef.addValueEventListener(object: ValueEventListener{
						override fun onDataChange(snapshot: DataSnapshot) {
							// checking if the donor and receiver are in the same city
							if(snapshot.child("city").value.toString() == receiverCity)
							{
								foodList.add(receiver_food_list(food.child("name").value.toString(), food.child("type").value.toString()))
								binding.rcvFoodList.adapter = adapter
							}
						}
						override fun onCancelled(error: DatabaseError) {
							Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
						}
					})
				}
			}
			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
			}
		})

		binding.rcvFoodList.adapter = adapter
		binding.rcvFoodList.layoutManager = LinearLayoutManager(this.requireContext())

		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}