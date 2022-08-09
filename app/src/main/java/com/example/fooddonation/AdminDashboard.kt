package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityAdminDashboardBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class AdminDashboard : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityAdminDashboardBinding =
			ActivityAdminDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)
		supportActionBar?.hide()
		val Auth = Firebase.auth
		val database = Firebase.database

		var donorCount = 0
		var receiverCount = 0
		var riderCount = 0

		/** Getting donors count **/
		database.getReference("Users").child("Donor")
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {
					donorCount = snapshot.childrenCount.toInt()
					binding.tvDonorCount.setText(donorCount.toString())
				}

				override fun onCancelled(error: DatabaseError) {
					Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
				}

			})

		/** Getting receiver count **/
		database.getReference("Users").child("Receiver")
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {
					receiverCount = snapshot.childrenCount.toInt()
					binding.tvReceiverCount.setText(receiverCount.toString())

				}

				override fun onCancelled(error: DatabaseError) {
					Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
				}

			})

		/** Getting rider count **/
		database.getReference("Users").child("Rider")
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {
					riderCount = snapshot.childrenCount.toInt()
					binding.tvRiderCount.setText(riderCount.toString())
					binding.tvUserCount.setText((donorCount + receiverCount+riderCount).toString())
				}

				override fun onCancelled(error: DatabaseError) {
					Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
				}

			})

		binding.tvDonorCount.setText(donorCount.toString())
		binding.tvReceiverCount.setText(receiverCount.toString())
		binding.tvRiderCount.setText(riderCount.toString())
		binding.tvUserCount.setText((donorCount + receiverCount+riderCount).toString())

		binding.btnAdd.setOnClickListener() {
			var i = Intent(this, RiderSignup::class.java)
			startActivity(i)
		}
		binding.btnRemove.setOnClickListener() {
			var i = Intent(this, RemoveUser::class.java)
			startActivity(i)
		}

		//for stablility
		Auth.signOut()
	}

}