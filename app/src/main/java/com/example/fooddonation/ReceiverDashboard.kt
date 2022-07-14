package com.example.fooddonation

import android.os.Bundle
import android.view.Menu
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

class ReceiverDashboard : AppCompatActivity() {

	private lateinit var appBarConfiguration: AppBarConfiguration
	private lateinit var binding: ActivityReceiverDashboardBinding

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityReceiverDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.appBarReceiverDashboard.toolbar2)

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
			AppBarConfiguration(setOf(R.id.Receiver_nav_home, R.id.Receiver_nav_history, R.id.Receiver_nav_logout),
			                    drawerLayout2)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView2.setupWithNavController(navController)
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