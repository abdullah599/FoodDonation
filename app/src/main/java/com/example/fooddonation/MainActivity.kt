package com.example.fooddonation

import android.animation.Animator
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        lateinit var auth: FirebaseAuth
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        var animView: LottieAnimationView = findViewById(R.id.animView)
        animView.addAnimatorListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                //Toast.makeText(applicationContext, "yes", Toast.LENGTH_SHORT).show()
                if (FirebaseAuth.getInstance().currentUser == null)
                {
                    val i = Intent(applicationContext, Login::class.java)
                startActivity(i)
                finish()
                }
                else{
                    auth= FirebaseAuth.getInstance()
                    val database = Firebase.database
                    /** Logging in if the user is a donor **/
                    var ref = database.getReference("Users").child("Donor")
                    ref.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(value in snapshot.children)
                            {
                                if (value.key == auth.currentUser?.uid)
                                {
                                    val i = Intent(applicationContext, DonorDashboard::class.java)
                                    startActivity(i)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                        }
                    })

                    /** Logging in if the user is a receiver **/
                    ref = database.getReference("Users").child("Receiver")
                    ref.addValueEventListener(object: ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for(value in snapshot.children)
                            {
                                if (value.key == auth.currentUser?.uid)
                                {
                                    val i = Intent(applicationContext, ReceiverDashboard::class.java)
                                    startActivity(i)
                                    finish()
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(this@MainActivity, "error", Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        })
    }
}