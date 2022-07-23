package com.example.fooddonation.models

class Food {
    var donor_id: String = ""
    var receiver_id: String = ""
    var name: String = ""
    var type: String = ""
    var expiry: String = ""
    var status: String = ""

    constructor(
        donor_id: String,
        receiver_id: String,
        name: String,
        type: String,
        expiry: String,
        status: String
    ) {
        this.donor_id = donor_id
        this.receiver_id = receiver_id
        this.name = name
        this.type = type
        this.expiry = expiry
        this.status = status
    }
}