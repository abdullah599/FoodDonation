package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // initializing auth
        Auth = Firebase.auth

        var intent=Intent(this, RiderDashboard::class.java)
        startActivity(intent)

        /** Checking if a user is already logged in - then logout the user **/
        if(Auth.currentUser != null)
        {
            Auth.signOut()
        }

        /** Checks for empty fields **/
        binding.btnLogin.setOnClickListener()
        {
            // Email
            if(binding.etEmail2.text.isEmpty())
            {
                binding.etEmail2.setError("Fill this field")
                binding.etEmail2.requestFocus()
                return@setOnClickListener
            }
            // Password
            if(binding.etPass2.text.isEmpty())
            {
                binding.etPass2.setError("Fill this field")
                binding.etPass2.requestFocus()
                return@setOnClickListener
            }

            /** User credentials **/
            val email = binding.etEmail2.text.toString()
            val pass = binding.etPass2.text.toString()

            /** Logging in the user **/
            Auth.signInWithEmailAndPassword(email, pass)
                // if the user is present in database
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val database = Firebase.database
                        /** Logging in if the user is a donor **/
                        var ref = database.getReference("Users").child("Donor")
                        ref.addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(value in snapshot.children)
                                {
                                    if (value.key == Auth.currentUser?.uid)
                                    {
                                        val i = Intent(applicationContext, DonorDashboard::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                //Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                            }
                        })

                        /** Logging in if the user is a receiver **/
                        ref = database.getReference("Users").child("Receiver")
                        ref.addValueEventListener(object: ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(value in snapshot.children)
                                {
                                    if (value.key == Auth.currentUser?.uid)
                                    {
                                        val i = Intent(applicationContext, ReceiverDashboard::class.java)
                                        startActivity(i)
                                        finish()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                //Toast.makeText(this, "error", Toast.LENGTH_SHORT).show()
                            }
                        })


                    } else {
                        /** Exceptions when login failed **/
                        if(task.exception is FirebaseAuthInvalidUserException)
                            Toast.makeText(this, "User does not exist", Toast.LENGTH_SHORT).show()
                        else
                            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show()
                    }
                }

        }

        /** If a user wants to sign up **/
        binding.tvSignupClick.setOnClickListener()
        {
            val i = Intent(this, SignUp::class.java)
            startActivity(i)
        }
    }
}