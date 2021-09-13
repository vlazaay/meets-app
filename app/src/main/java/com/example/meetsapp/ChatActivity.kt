package com.example.meetsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.cometchat.pro.constants.CometChatConstants
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.Conversation
import com.cometchat.pro.models.Group
import com.cometchat.pro.models.TextMessage
import com.cometchat.pro.models.User
import com.cometchat.pro.uikit.ui_components.messages.message_list.CometChatMessageListActivity
import com.cometchat.pro.uikit.ui_resources.constants.UIKitConstants
import com.example.meetsapp.databinding.ActivityChatBinding

class ChatActivity : AppCompatActivity() {
    lateinit var bindingClass : ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityChatBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        val user = User()
        user.uid = "pair" // Replace with your uid for the user to be created.
        user.name = "YourPair" // Repla
//        CometChatUserList.setItemClickListener(object : OnItemClickListener<Any>() {
//            override fun OnItemClick(t: Any, position: Int) {
//                userIntent(t as User)
//            }
//        })
        userIntent(user)


    }

    override fun onResume() {
        super.onResume()
        finish()
    }


    fun userIntent(user: User) {
        val intent = Intent(this@ChatActivity, CometChatMessageListActivity::class.java)
        intent.putExtra(UIKitConstants.IntentStrings.UID, user.uid)
        intent.putExtra(UIKitConstants.IntentStrings.AVATAR, user.avatar)
        intent.putExtra(UIKitConstants.IntentStrings.STATUS, user.status)
        intent.putExtra(UIKitConstants.IntentStrings.NAME, user.name)
        intent.putExtra(UIKitConstants.IntentStrings.TYPE, CometChatConstants.RECEIVER_TYPE_USER)
        startActivity(intent)
    }

}