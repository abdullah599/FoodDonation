package com.example.fooddonation

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.fooddonation.databinding.ActivityRemoveUserBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase


class RemoveUser : AppCompatActivity() {
	@SuppressLint("ResourceAsColor")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		val binding: ActivityRemoveUserBinding = ActivityRemoveUserBinding.inflate(layoutInflater)
		setContentView(binding.root)
		supportActionBar?.hide()

		lateinit var type:String
		lateinit var yesno:String
		binding.btnRemove.setOnClickListener {
			if(binding.etEmail.text.isEmpty())
			{
				binding.etEmail.setError("Fill this field")
				binding.etEmail.requestFocus()
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
				if(binding.rbDonor.isChecked)
					type = "Donor"
				else if(binding.rbReceiver.isChecked)
					type = "Receiver"
				else if(binding.rbRider.isChecked)
					type = "Rider"
			}
			/** If no radio button is checked **/
			if (binding.radioGroup2.getCheckedRadioButtonId() == -1)
			{
				Toast.makeText(this, "Select atleast one", Toast.LENGTH_SHORT).show()
				binding.radioGroup2.setBackgroundColor(R.color.red)
				return@setOnClickListener
			}
			else
			{
				yesno = if(binding.rbYes.isChecked)
					"Yes"
				else
					"No"
			}

			if(yesno=="No"){
				var i=Intent(this,AdminDashboard::class.java)
				Toast.makeText(this, "No one is removed", Toast.LENGTH_SHORT).show()
				startActivity(i)

			}
			else{
				/** Logging in if the user is a receiver **/
				var ref = database.getReference("Users").child(type)
				ref.addValueEventListener(object: ValueEventListener {
					override fun onDataChange(snapshot: DataSnapshot) {
						for(user in snapshot.children)
						{
							if (user.child("email").value == binding.etEmail.text.toString())
							{

								val Ref = database.getReference("Users").child(type).child(user.key!!)
									Ref.child("isban").setValue("yes")
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
}