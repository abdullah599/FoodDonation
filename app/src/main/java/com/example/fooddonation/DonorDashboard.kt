package com.example.fooddonation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddonation.databinding.ActivityDonorDashboardBinding
import com.google.android.gms.common.util.CollectionUtils.setOf

class DonorDashboard : AppCompatActivity() {

	private lateinit var binding: ActivityDonorDashboardBinding
	private lateinit var appBarConfiguration: AppBarConfiguration


	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = ActivityDonorDashboardBinding.inflate(layoutInflater)
		setContentView(binding.root)

		setSupportActionBar(binding.appBarDonorDashboard.toolbar)

		binding.appBarDonorDashboard.fab.setOnClickListener { view ->
			Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
				.setAction("Action", null).show()
		}
		val drawerLayout: DrawerLayout = binding.drawerLayout
		val navView1: NavigationView = binding.navView
		val navController = findNavController(
			R.id.nav_host_fragment_content_donor_dashboard) // Passing each menu ID as a set of Ids because each
		// menu should be considered as top level destinations.
		appBarConfiguration =
			AppBarConfiguration(setOf(R.id.Donor_nav_home, R.id.Donor_nav_history, R.id.Donor_nav_logout),
			                    drawerLayout)
		setupActionBarWithNavController(navController, appBarConfiguration)
		navView1.setupWithNavController(navController)

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