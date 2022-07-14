package com.example.fooddonation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityLoginBinding
import com.example.fooddonation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        // initializing auth
        auth = Firebase.auth

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
            auth.signInWithEmailAndPassword(email, pass)
                // if the user is present in database
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, auth.currentUser?.uid.toString(), Toast.LENGTH_SHORT).show()

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