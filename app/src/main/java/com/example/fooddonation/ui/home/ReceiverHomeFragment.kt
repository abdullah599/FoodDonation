package com.example.fooddonation.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
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

		val textView: TextView = binding.textHome2

			textView.text = "receiver home"

		return root
	}

	override fun onDestroyView() {
		super.onDestroyView()
		_binding = null
	}
}