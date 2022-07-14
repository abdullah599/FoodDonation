package com.example.fooddonation.models

import android.provider.ContactsContract

class User {
    var name: String = ""
    var email: String = ""
    var city:String = ""
    var address:String = ""

   constructor(name: String, email:String, city:String, address:String)
   {
       this.name = name
       this.email = email
       this.city = city
       this.address = address
   }
}