package com.example.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivitySignUpBinding


class SignUp : AppCompatActivity() {
    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivitySignUpBinding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
var intent=Intent(this, DonorDashboard::class.java)
        startActivity(intent)
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

        /** If a user has already have an account and want to login **/
        binding.tvLoginClick.setOnClickListener()
        {
            val i = Intent(this, Login::class.java)
            startActivity(i)
        }

    }
}