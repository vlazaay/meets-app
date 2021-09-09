package com.example.meetsapp.model

data class User(
    var deviceID: String,
    var nickname: String,
    var name: String,
    var age: Int,
    var zodiac: String,
    var meal_preferences: String,
    var human_preferences: String,
    var photo: String,
    var gender: String,
)
