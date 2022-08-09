package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityRiderSignup2Binding
import com.example.fooddonation.models.Rider
import com.example.fooddonation.models.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RiderSignup2 : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityRiderSignup2Binding = ActivityRiderSignup2Binding.inflate(layoutInflater)
		setContentView(binding.root)
		supportActionBar?.hide()

		// initializing auth
		Auth = Firebase.auth

		/** When sign up button is clicked **/
		binding.btnRadd.setOnClickListener()
		{
			/** Checks for empty fields **/
			// City
			if(binding.etRcnic.text.isEmpty())
			{
				binding.etRcnic.setError("Fill this field")
				binding.etRcnic.requestFocus()
				return@setOnClickListener
			}
			// House No
			if(binding.etBikeno.text.isEmpty())
			{
				binding.etBikeno.setError("Fill this field")
				binding.etBikeno.requestFocus()
				return@setOnClickListener
			}
			// Street
			if(binding.retAdd.text.isEmpty())
			{
				binding.retAdd.setError("Fill this field")
				binding.retAdd.requestFocus()
				return@setOnClickListener
			}
			//city
			if(binding.retCity.text.isEmpty())
			{
				binding.retCity.setError("Fill this field")
				binding.retCity.requestFocus()
				return@setOnClickListener
			}


			/** Data of the user to store in database **/
			val name: String = intent.extras?.get("rider_name") as String
			val email: String = intent.extras?.get("rider_email") as String
			val pass: String = intent.extras?.get("rider_pass") as String
			val type: String = "Rider"
			val cnic=binding.etRcnic.text.toString()
			val vehicleno=binding.etBikeno.text.toString()
			val city:String = binding.retCity.text.toString()
			val address:String = binding.retAdd.text.toString()

			/** Creating a new user using the email and password **/
			Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
				// When a user is created successfully
				if(it.isSuccessful)
				{
					// Adding more details for the user in database using uid
					val user =Rider(name, email, cnic, vehicleno,address,city)
					val database = Firebase.database

					Auth.currentUser?.let { it1 ->
						database.getReference("Users").child(type).child(
							it1.uid).setValue(user).addOnSuccessListener {
							Toast.makeText(this, "user registered", Toast.LENGTH_SHORT).show()

						}.addOnFailureListener(this, OnFailureListener {
							Toast.makeText(this, "error-database", Toast.LENGTH_SHORT).show()
						})
					}
				}
				else
				{
					/** Exceptions when sign up failed **/
					if(it.exception is FirebaseAuthWeakPasswordException)
						Toast.makeText(this, "Password should be at least of 6 characters", Toast.LENGTH_SHORT).show()
					else if(it.exception is FirebaseAuthUserCollisionException)
						Toast.makeText(this, "This email is already in use", Toast.LENGTH_SHORT).show()
					else if(it.exception is FirebaseAuthInvalidCredentialsException)
						Toast.makeText(this, "Enter a valid email", Toast.LENGTH_SHORT).show()
					else
						Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
				}
			}
			Auth.signOut()
			var intent=Intent(this,AdminDashboard::class.java)
			startActivity(intent)
		}



	}
}