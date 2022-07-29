package com.example.fooddonation

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.fooddonation.databinding.ActivityFoodFormBinding
import com.example.fooddonation.models.Food
import com.example.fooddonation.models.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import javax.xml.datatype.DatatypeConstants.MONTHS
import kotlin.math.exp


class FoodForm : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding:ActivityFoodFormBinding = ActivityFoodFormBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        /** For displaying Date Picker **/
        val calendar: Calendar = Calendar.getInstance()
        var year = calendar.get(Calendar.YEAR)
        var month = calendar.get(Calendar.MONTH)
        var day = calendar.get(Calendar.DAY_OF_MONTH)


        binding.etExp.showSoftInputOnFocus = false
        binding.etExp.setOnClickListener()
        {
            val dpd = DatePickerDialog(this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                // Display Selected date in textbox
                month = monthOfYear + 1
                binding.etExp.setText("$dayOfMonth/$month/$year")

            }, year, month, day)
            dpd.show()
        }

        /** Getting address from database **/
        val auth = Firebase.auth
        val database = Firebase.database
        var ref = auth.currentUser?.let { database.getReference("Users").child("Donor").child(it.uid) }
        ref?.addValueEventListener(object : ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                binding.etCity.setText(snapshot.child("city").value.toString())
                binding.etAdd.setText(snapshot.child("address").value.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "error", Toast.LENGTH_SHORT).show()
            }

        })

        /** When donate button is clicked **/
        binding.btnDonate.setOnClickListener()
        {
            /** Checks for empty fields **/
            // Food Name
            if(binding.etFname.text.isEmpty())
            {
                binding.etFname.setError("Fill this field")
                binding.etFname.requestFocus()
                return@setOnClickListener
            }
            // Food Type
            if(binding.etFtype.text.isEmpty())
            {
                binding.etFtype.setError("Fill this field")
                binding.etFtype.requestFocus()
                return@setOnClickListener
            }
            // Food Expiry Date
            if(binding.etExp.text.isEmpty())
            {
                binding.etExp.setError("Fill this field")
                binding.etExp.requestFocus()
                return@setOnClickListener
            }
            // Address
            if(binding.etAdd.text.isEmpty())
            {
                binding.etAdd.setError("Fill this field")
                binding.etAdd.requestFocus()
                return@setOnClickListener
            }
            // City
            if(binding.etCity.text.isEmpty())
            {
                binding.etCity.setError("Fill this field")
                binding.etCity.requestFocus()
                return@setOnClickListener
            }

            /** Updating the address and city for the donor **/
            ref = auth.currentUser?.let { database.getReference("Users").child("Donor").child(it.uid) }
            ref?.child("city")?.setValue(binding.etCity.text.toString())
            ref?.child("address")?.setValue(binding.etAdd.text.toString())

            /** Food details to store in database **/
            val fname = binding.etFname.text.toString()
            val ftype = binding.etFtype.text.toString()
            val expiry = binding.etExp.text.toString()
            val donor_id = auth.currentUser?.uid.toString()
            val status = "Pending"
            // Food Object
            val food:Food = Food(donor_id,"", fname, ftype, expiry, status)

            ref = database.getReference("Food").push()      // automatically generates unique id
            ref?.setValue(food)

        }
    }
}