package com.example.fooddonation.models

class Rider {
	var name: String = ""
	var email: String = ""
	var cnic:String = ""
	var vehicleno:String = ""
	var address:String = ""
	var city:String = ""

	constructor(name: String, email:String, cnic:String, vehicleno:String,address:String,city:String)
	{
		this.name = name
		this.email = email
		this.cnic = cnic
		this.vehicleno=vehicleno
		this.address = address
		this.city=city
	}
}