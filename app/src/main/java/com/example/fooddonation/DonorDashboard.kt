package com.example.fooddonation

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.fooddonation.databinding.ActivityDonorDashboardBinding
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


private lateinit var auth: FirebaseAuth;


class DonorDashboard : AppCompatActivity() {

	private lateinit var binding: ActivityDonorDashboardBinding
	private lateinit var appBarConfiguration: AppBarConfiguration


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityDonorDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.appBarDonorDashboard.toolbar)

		auth = Firebase.auth

		binding.appBarDonorDashboard.fab.setOnClickListener { view ->
			Toast.makeText(this,"Intent will be added to add food",Toast.LENGTH_SHORT).show()
		}


		val colorDrawable = ColorDrawable(Color.parseColor("#DF0A0A"))

		// Set BackgroundDrawable

		// Set BackgroundDrawable
		supportActionBar!!.setBackgroundDrawable(colorDrawable)


		val drawerLayout: DrawerLayout = binding.drawerLayout
		val navView1: NavigationView = binding.navView
		val navController = findNavController(
			R.id.nav_host_fragment_content_donor_dashboard) // Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		appBarConfiguration =
			AppBarConfiguration(
				mutableSetOf(R.id.Donor_nav_home, R.id.Donor_nav_history, R.id.Donor_nav_logout),
				drawerLayout)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView1.setupWithNavController(navController)

		/** Setting data (Email and Name) in header of drawer menu **/
		val header: View = navView1.getHeaderView(0)
		// Email from authentication
		header.findViewById<TextView>(R.id.tv_header_email1).text = auth.currentUser?.email ?: "No email"
		// Name from database
		val ref = auth.currentUser?.let { database.getReference("Users").child("Donor").child(it.uid).child("name") }
			// Getting data from database
		ref?.addValueEventListener(object: ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				header.findViewById<TextView>(R.id.tv_header_name1).text = snapshot.value.toString()
			}

			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
			}
		})

	}


//	override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
//		menuInflater.inflate(R.menu.donor_dashboard, menu)
//		return true
//	}

	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_content_donor_dashboard)
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}


}