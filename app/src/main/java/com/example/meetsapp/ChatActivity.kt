package com.example.meetsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meetsapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var bindingClass : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityChatBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
    }
}