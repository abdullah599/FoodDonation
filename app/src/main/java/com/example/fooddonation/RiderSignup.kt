package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityRiderSignupBinding
import com.example.fooddonation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RiderSignup : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding:ActivityRiderSignupBinding= ActivityRiderSignupBinding.inflate(layoutInflater)
		setContentView(binding.root)
		supportActionBar?.hide()

		Auth = Firebase.auth

		binding.rbtnNext.setOnClickListener()
		{
			/** to move to next activity i.e Sign Up 2 **/
			val i = Intent(this, RiderSignup2::class.java)



			/** Checks for empty fields **/
			// Name
			if(binding.retName.text.isEmpty())
			{
				binding.retName.setError("Fill this field")
				binding.retName.requestFocus()
				return@setOnClickListener
			}
			// Email
			if(binding.retEmail.text.isEmpty())
			{
				binding.retEmail.setError("Fill this field")
				binding.retEmail.requestFocus()
				return@setOnClickListener
			}
			// Password
			if(binding.retPass.text.isEmpty())
			{
				binding.retPass.setError("Fill this field")
				binding.retPass.requestFocus()
				return@setOnClickListener
			}
			// Confirm Password
			if(binding.retCpass.text.isEmpty())
			{
				binding.retCpass.setError("Fill this field")
				binding.retCpass.requestFocus()
				return@setOnClickListener
			}
			// Password == Confirm Password
			if(binding.retPass.text.toString() != binding.retCpass.text.toString())
			{
				binding.retCpass.setError("Password doesnt matches")
				binding.retCpass.requestFocus()
				return@setOnClickListener
			}



			/** Sending fields data with intent to signup 2 **/
			i.putExtra("rider_name", binding.retName.text.toString())
			i.putExtra("rider_email", binding.retEmail.text.toString())
			i.putExtra("rider_pass", binding.retPass.text.toString())


			startActivity(i)
		}


	}
}