package com.example.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

private lateinit var auth: FirebaseAuth;

class SignUp : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        Auth = Firebase.auth
//        var intent=Intent(this, ReceiverDashboard::class.java)
//        startActivity(intent)
        binding.btnNext.setOnClickListener()
        {
            /** to move to next activity i.e Sign Up 2 **/
            val i = Intent(this, SignUp2::class.java)

            var type:String = ""    // for donor or receiver

            /** Checks for empty fields **/
            // Name
            if(binding.etName.text.isEmpty())
            {
                binding.etName.setError("Fill this field")
                binding.etName.requestFocus()
                return@setOnClickListener
            }
            // Email
            if(binding.etEmail.text.isEmpty())
            {
                binding.etEmail.setError("Fill this field")
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }
            // Password
            if(binding.etPass.text.isEmpty())
            {
                binding.etPass.setError("Fill this field")
                binding.etPass.requestFocus()
                return@setOnClickListener
            }
            // Confirm Password
            if(binding.etCpass.text.isEmpty())
            {
                binding.etCpass.setError("Fill this field")
                binding.etCpass.requestFocus()
                return@setOnClickListener
            }
            // Password == Confirm Password
            if(binding.etPass.text.toString() != binding.etCpass.text.toString())
            {
                binding.etCpass.setError("Password doesnt matches")
                binding.etCpass.requestFocus()
                return@setOnClickListener
            }

            /** If no radio button is checked **/
            if (binding.radioGroup.getCheckedRadioButtonId() == -1)
            {
                Toast.makeText(this, "Select atleast one", Toast.LENGTH_SHORT).show()
                binding.radioGroup.setBackgroundColor(R.color.red)
                return@setOnClickListener
            }
            else
            {
                type = if(binding.rbDonor.isChecked)
                    "Donor"
                else
                    "Receiver"
            }

            /** Sending fields data with intent to signup 2 **/
            i.putExtra("user_name", binding.etName.text.toString())
            i.putExtra("user_email", binding.etEmail.text.toString())
            i.putExtra("user_pass", binding.etPass.text.toString())
            i.putExtra("user_type", type)

            startActivity(i)
        }

        /** If a user already have an account and want to login **/
        binding.tvLoginClick.setOnClickListener()
        {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

    }

    /** Checking if a user is logged in or not **/
    override fun onStart() {
        super.onStart()
        if(Auth.currentUser != null)
        {
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
        }
    }
}