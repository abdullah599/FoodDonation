package com.example.fooddonation.ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.DonorHistoryAdapter
import com.example.fooddonation.Adapter_Dataclasses.donor_history_data
import com.example.fooddonation.databinding.FragmentDonorHistoryBinding
import com.example.fooddonation.models.Food
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class DonorHistoryFragment : Fragment() {

	private var _binding: FragmentDonorHistoryBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


		_binding = FragmentDonorHistoryBinding.inflate(inflater, container, false)
		val root: View = binding.root

		// auth reference
		val auth = Firebase.auth

		/** Food List and adapter for recycler view **/
		val foodList: ArrayList<donor_history_data> = ArrayList()
		val adapter = DonorHistoryAdapter(foodList,this.requireContext())

		/** Database call **/
		val database = Firebase.database
		val ref = database.getReference("Food")
		ref.addValueEventListener(object: ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				for (food in snapshot.children)
				{
					// checking if the food is donated by this donor
					if(food.child("donor_id").value == auth.currentUser?.uid)
					{
						foodList.add(donor_history_data(food.child("name").value.toString(), food.child("type").value.toString(), food.child("status").value.toString()))
					}
				}
				// updating adapter
				binding.rvHistory.adapter = adapter
			}

			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
			}

		})

		binding.rvHistory.adapter=adapter
		binding.rvHistory.layoutManager= LinearLayoutManager(this.requireContext())

		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}