package com.example.fooddonation

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddonation.databinding.ActivityReceiverDashboardBinding
import com.google.android.gms.common.util.CollectionUtils.setOf
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;
val database = Firebase.database


class ReceiverDashboard : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivityReceiverDashboardBinding
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		binding = ActivityReceiverDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.appBarReceiverDashboard.toolbar2)

		auth = Firebase.auth		// auth initializing

		binding.appBarReceiverDashboard.fab2.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		}
		val drawerLayout2: DrawerLayout = binding.drawerLayout2
		val navView2: NavigationView = binding.navView2
		val navController = findNavController(
			R.id.nav_host_fragment_content_receiver_dashboard) // Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		appBarConfiguration =
			AppBarConfiguration(mutableSetOf(R.id.Receiver_nav_home, R.id.Receiver_nav_history, R.id.Receiver_nav_logout),
			                    drawerLayout2)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView2.setupWithNavController(navController)

		/** Setting data in header of drawer menu **/
		val header: View = navView2.getHeaderView(0)
		// Email from authentication
		header.findViewById<TextView>(R.id.tv_header_email).text = auth.currentUser?.email ?: "No email"
		// Name from database
		val ref = auth.currentUser?.let { database.getReference("Users").child("Receiver").child(it.uid).child("name") }
		// Getting data from database
		ref?.addValueEventListener(object: ValueEventListener{
			override fun onDataChange(snapshot: DataSnapshot) {
				header.findViewById<TextView>(R.id.tv_header_name).text = snapshot.value.toString()
			}

			override fun onCancelled(error: DatabaseError) {
				Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
			}
		})

	}

	//		override fun onCreateOptionsMenu(menu: Menu): Boolean { // Inflate the menu; this adds items to the action bar if it is present.
	//			menuInflater.inflate(R.menu.receiver_dashboard, menu)
	//			return true
	//		}
	override fun onSupportNavigateUp(): Boolean {
		val navController = findNavController(R.id.nav_host_fragment_content_receiver_dashboard)
		return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
	}
}