package com.example.fooddonation.ui.home

import android.annotation.SuppressLint
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
import java.text.SimpleDateFormat
import java.time.LocalDate.now
import java.util.*
import kotlin.collections.ArrayList

class ReceiverHomeFragment : Fragment(), ReceiverFoodListAdapter.OnBtnClick {

	private var _binding: FragmentReceiverHomeBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!
	val auth = Firebase.auth
	val database = Firebase.database

	/** Food List and adapter for recycler view **/
	lateinit var foodList: ArrayList<receiver_food_list>
	lateinit var adapter:ReceiverFoodListAdapter

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

		_binding = FragmentReceiverHomeBinding.inflate(inflater, container, false)
		val root: View = binding.root

		/** Food List and adapter for recycler view **/
		 foodList = ArrayList()
		 adapter = ReceiverFoodListAdapter(foodList,this.requireContext(), this)

		/** Database call **/
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
				foodList.clear()
				for (food in snapshot.children)
				{
					// Getting donor's city
					val tempRef = database.getReference("Users").child("Donor").child(food.child("donor_id").value.toString())
					tempRef.addValueEventListener(object: ValueEventListener{
						override fun onDataChange(snapshot: DataSnapshot) {
							// checking if the donor and receiver are in the same city
							if(snapshot.child("city").value.toString() == receiverCity && food.child("status").value.toString() == "Pending")
							{
								foodList.add(receiver_food_list(food.child("name").value.toString(), food.child("type").value.toString(), food.key.toString()))
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

	override fun onButtonClick(pos: Int, key:String) {
		val ref = database.getReference("Food").child(key)
		ref.child("status").setValue("Donated")
		ref.child("receiver_id").setValue(auth.currentUser?.uid)

		// as the list will be populated again
		foodList.clear()
		binding.rcvFoodList.adapter = adapter
	}
}