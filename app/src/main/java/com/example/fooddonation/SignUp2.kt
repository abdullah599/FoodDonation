package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivitySignUp2Binding
import com.example.fooddonation.models.User
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;

class SignUp2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignUp2Binding = ActivitySignUp2Binding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // initializing auth
        Auth = Firebase.auth

        /** When sign up button is clicked **/
        binding.btnSignup.setOnClickListener()
        {
            /** Checks for empty fields **/
            // City
            if(binding.etCity.text.isEmpty())
            {
                binding.etCity.setError("Fill this field")
                binding.etCity.requestFocus()
                return@setOnClickListener
            }
            // House No
            if(binding.etHouse.text.isEmpty())
            {
                binding.etHouse.setError("Fill this field")
                binding.etHouse.requestFocus()
                return@setOnClickListener
            }
            // Street
            if(binding.etStreet.text.isEmpty())
            {
                binding.etStreet.setError("Fill this field")
                binding.etStreet.requestFocus()
                return@setOnClickListener
            }

            /** Data of the user to store in database **/
            val name: String = intent.extras?.get("user_name") as String
            val email: String = intent.extras?.get("user_email") as String
            val pass: String = intent.extras?.get("user_pass") as String
            val type: String = intent.extras?.get("user_type") as String
            val city:String = binding.etCity.text.toString()
            val address:String = binding.etHouse.text.toString() + " " + binding.etStreet.text.toString()

            /** Creating a new user using the email and password **/
            Auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener {
                // When a user is created successfully
                if(it.isSuccessful)
                {
                    // Adding more details for the user in database using uid
                    val user: User = User(name, email, city, address)
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
        }

        /** If a user has already have an account and want to login **/
        binding.tvLoginClick2.setOnClickListener()
        {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

    }
}