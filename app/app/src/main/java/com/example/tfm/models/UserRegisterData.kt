package com.example.tfm.models

data class UserRegisterData(
    var username :String,
    var email :String,
    var password :String,
    var paisOrigen:String,
    var salt:String,
)
