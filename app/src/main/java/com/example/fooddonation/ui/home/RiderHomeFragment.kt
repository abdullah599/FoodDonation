package com.example.fooddonation.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fooddonation.Adapter_Dataclasses.RiderOrderListAdapter
import com.example.fooddonation.Adapter_Dataclasses.rider_order_list
import com.example.fooddonation.databinding.FragmentRiderHomeBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class RiderHomeFragment : Fragment(), RiderOrderListAdapter.OnRiderBtnClick {

    private var _binding: FragmentRiderHomeBinding? = null
    var donor_add = ""
    var receiver_add = ""
    var fkey=""

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val auth = Firebase.auth
    val database = Firebase.database

    /** Food List and adapter for recycler view **/
    lateinit var orderList: ArrayList<rider_order_list>
    lateinit var adapter: RiderOrderListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentRiderHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        /** Food List and adapter for recycler view **/
        orderList = ArrayList()
        adapter = RiderOrderListAdapter(orderList, this.requireContext(), this)

        /** Getting rider's city **/
        val ref =
            auth.currentUser?.let { database.getReference("Users").child("Rider").child(it.uid) }

        var riderCity = ""

        ref?.addValueEventListener(object: ValueEventListener
        {
            override fun onDataChange(snapshot: DataSnapshot) {
                riderCity = snapshot.child("city").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        /** Getting all the orders **/
        database.getReference("Food").addValueEventListener(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList.clear()
               for(food in snapshot.children)
               {
                   // only requested food and food from same city as rider
                   if(food.child("status").value.toString() != "Finding Rider" || food.child("city").value.toString() != riderCity)
                       continue



                   /** Getting donor address **/
                   val donor_ref = database.getReference("Users").child("Donor").child(food.child("donor_id").value.toString())
                   donor_ref.addValueEventListener(object: ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                           donor_add = snapshot.child("address").value.toString()
                       }
                       override fun onCancelled(error: DatabaseError) {
                           Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                       }

                   })

                   /** Getting receiver address **/
                   val receiver_ref = database.getReference("Users").child("Receiver").child(food.child("receiver_id").value.toString())
                   receiver_ref.addValueEventListener(object: ValueEventListener{
                       override fun onDataChange(snapshot: DataSnapshot) {
                           receiver_add = snapshot.child("address").value.toString()

                           orderList.add(rider_order_list(donor_add, receiver_add, food.key.toString()))
                           binding.rcvOrderList.adapter = adapter
                       }
                       override fun onCancelled(error: DatabaseError) {
                           Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                       }

                   })

               }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }

        })

        binding.rcvOrderList.adapter = adapter
        binding.rcvOrderList.layoutManager = LinearLayoutManager(this.requireContext())

        binding.dlvBtn.setOnClickListener {
            val foodRef = database.getReference("Food").child(fkey)
            foodRef.child("status").setValue("Donated")
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("SetTextI18n")
    override fun onButtonClick(pos: Int, key: String) {
        val foodRef = database.getReference("Food").child(key)
        foodRef.child("status").setValue("Delivering")
        foodRef.child("rider_id").setValue(auth.currentUser?.uid)

        fkey=key

        hideRecyclerView()
        binding.tv31.text="From: "+donor_add
        binding.tv32.text="To: "+receiver_add

        foodRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.child("status").value.toString() != "Delivering") {

                    binding.rcvOrderList.adapter?.notifyDataSetChanged()

                        showRecyclerView()


                }
                else{
                    hideRecyclerView()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })

    }

    fun showRecyclerView() {

        binding.rcvOrderList.visibility = View.VISIBLE
        binding.textView3.visibility = View.VISIBLE
        binding.animationView3.visibility = View.GONE
        binding.tv31.visibility = View.GONE
        binding.tv32.visibility = View.GONE
        binding.rImg.visibility = View.GONE
        binding.dlvBtn.visibility=View.GONE


    }

    fun hideRecyclerView() {
        binding.rcvOrderList.visibility = View.GONE
        binding.textView3.visibility = View.GONE
        binding.animationView3.visibility = View.VISIBLE
        binding.tv31.visibility = View.VISIBLE
        binding.tv32.visibility = View.VISIBLE
        binding.rImg.visibility = View.VISIBLE
        binding.dlvBtn.visibility=View.VISIBLE

    }
}