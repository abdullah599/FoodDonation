package com.example.fooddonation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.DonorHistoryAdapter
import com.example.fooddonation.Adapter_Dataclasses.ReceiverFoodListAdapter
import com.example.fooddonation.Adapter_Dataclasses.donor_history_data
import com.example.fooddonation.Adapter_Dataclasses.receiver_food_list
import com.example.fooddonation.databinding.FragmentReceiverHomeBinding

class ReceiverHomeFragment : Fragment() {

	private var _binding: FragmentReceiverHomeBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
		val homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

		_binding = FragmentReceiverHomeBinding.inflate(inflater, container, false)
		val root: View = binding.root

		val foodList: ArrayList<receiver_food_list> = ArrayList()
		val adapter = ReceiverFoodListAdapter(foodList,this.requireContext())
		foodList.add(receiver_food_list("Pizza", "Home-Made"))
		foodList.add(receiver_food_list("Kheer", "Desi"))
		foodList.add(receiver_food_list("Burger", "Fast food"))
		foodList.add(receiver_food_list("Pizza", "Home-Made"))
		foodList.add(receiver_food_list("Karahi", "Desi"))
		foodList.add(receiver_food_list("Burger", "Fast food"))

		binding.rcvFoodList.adapter = adapter
		binding.rcvFoodList.layoutManager = LinearLayoutManager(this.requireContext())

		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}