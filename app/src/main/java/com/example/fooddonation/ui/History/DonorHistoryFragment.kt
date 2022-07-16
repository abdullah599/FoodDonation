package com.example.fooddonation.ui.History

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.DonorHistoryAdapter
import com.example.fooddonation.Adapter_Dataclasses.donor_history_data
import com.example.fooddonation.databinding.FragmentDonorHistoryBinding


class DonorHistoryFragment : Fragment() {

	private var _binding: FragmentDonorHistoryBinding? = null

	// This property is only valid between onCreateView and
	// onDestroyView.
	private val binding get() = _binding!!

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {


		_binding = FragmentDonorHistoryBinding.inflate(inflater, container, false)
		val root: View = binding.root

		val textView: TextView = binding.textHistory
		textView.text="History"
		var array= mutableListOf(donor_history_data("Pizza", "Home Made","Not delivered"))
		val adapter=DonorHistoryAdapter(array,this.requireContext())
		binding.rvHistory.adapter=adapter
		binding.rvHistory.layoutManager= LinearLayoutManager(this.requireContext())



		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}