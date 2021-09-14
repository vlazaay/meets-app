package com.example.meetsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.meetsapp.databinding.ActivityChatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlin.random.Random

class ChatActivity : AppCompatActivity() {
    lateinit var bindingClass : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityChatBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

        var rand = (0..100000).random()
        val database = Firebase.database
        val myRef = database.getReference(ApplicationClass.userData.deviceID + rand)
        bindingClass.bsend.setOnClickListener{
           // Toast.makeText(applicationContext, "Suc", Toast.LENGTH_SHORT).show()
            myRef.setValue(bindingClass.edMessage.text.toString())
        }
        onChangeListener(myRef)


    }

    private fun onChangeListener(dRef: DatabaseReference){
        dRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                bindingClass.apply {
                    if (snapshot.value!=null) {
                        rcView.append("\n")
                        rcView.append("You: ${snapshot.value.toString()}")
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                dRef.setValue(null)
            }

        })
    }
//    override fun onResume() {
//        super.onResume()
//        finish()
//    }


}