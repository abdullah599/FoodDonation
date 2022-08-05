package com.example.fooddonation.ui.home

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
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
			@RequiresApi(Build.VERSION_CODES.O)
			override fun onDataChange(snapshot: DataSnapshot) {
				foodList.clear()
				for (food in snapshot.children)
				{
					// Checking expiry for the food
					CheckExpiry(food.key, food.child("expiry").value.toString(), food.child("status").value.toString())

					// Getting donor's city
					val tempRef = database.getReference("Users").child("Donor").child(food.child("donor_id").value.toString())
					tempRef.addValueEventListener(object: ValueEventListener{
						override fun onDataChange(snapshot: DataSnapshot) {
							// checking if the donor and receiver are in the same city
							if(snapshot.child("city").value.toString() == receiverCity && food.child("status").value.toString() == "Pending")
							{
								foodList.add(receiver_food_list(food.child("name").value.toString(), food.child("type").value.toString(), food.key.toString()))

							}
//							if(foodList.isNotEmpty())
//								showRecyclerView()
//							else
//								noFoodAvailable()
						}
						override fun onCancelled(error: DatabaseError) {
							Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
						}
					})
				}

				binding.rcvfoodList.adapter = adapter

			}
			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
			}
		})

		binding.rcvfoodList.adapter = adapter
		binding.rcvfoodList.layoutManager = LinearLayoutManager(this.requireContext())

		return root
	}

	@SuppressLint("SimpleDateFormat")
	private fun CheckExpiry(key: String?, date: String, status: String) {
		val exp:Date = SimpleDateFormat("dd/mm/yyyy").parse(date) as Date

		/** Getting current Date **/
		val calendar:Calendar = Calendar.getInstance()
		val year = calendar.get(Calendar.YEAR)
		val month = calendar.get(Calendar.MONTH) + 1
		val day = calendar.get(Calendar.DAY_OF_MONTH)

		val current = "$day/$month/$year"
		val curr:Date = SimpleDateFormat("dd/mm/yyyy").parse(current) as Date

		/** If the food item is expired and not donated yet **/
		if(curr.after(exp) && status == "Pending")
		{
			val ref = Firebase.database.getReference("Food").child(key.toString()).child("status")
			ref.setValue("Expired")
		}
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}

	/** Definition for the interface function declared in rcv adapter **/
	override fun onButtonClick(pos: Int, key:String) {
		val foodRef = database.getReference("Food").child(key)
		foodRef.child("status").setValue("Finding Rider")
		foodRef.child("receiver_id").setValue(auth.currentUser?.uid)

		foodList.remove(foodList[pos])

		hideRecyclerView()

		foodRef.addValueEventListener(object: ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				if(snapshot.child("status").value.toString() != "Finding Rider")
				{
						showRecyclerView()


				}
			}
			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
			}
		})


	}
	fun showRecyclerView(){

		binding.rcvfoodList.visibility=View.VISIBLE
		binding.textView.visibility=View.VISIBLE
		binding.animationView2.visibility=View.GONE
		binding.tv2.visibility=View.GONE
		binding.tv3.visibility=View.GONE
		binding.riderImg.visibility=View.GONE
		binding.tv4.visibility=View.GONE

	}
	fun hideRecyclerView(){
		binding.rcvfoodList.visibility=View.GONE
		binding.textView.visibility=View.GONE
		binding.animationView2.visibility=View.VISIBLE
		binding.tv2.visibility=View.VISIBLE
		binding.tv3.visibility=View.VISIBLE
		binding.riderImg.visibility=View.VISIBLE
		binding.tv4.visibility=View.GONE
	}
	fun noFoodAvailable(){
		binding.rcvfoodList.visibility=View.GONE
		binding.textView.visibility=View.VISIBLE
		binding.animationView2.visibility=View.GONE
		binding.tv2.visibility=View.GONE
		binding.tv3.visibility=View.GONE
		binding.riderImg.visibility=View.GONE
		binding.tv4.visibility=View.VISIBLE
	}

}