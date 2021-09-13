package com.example.meetsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.meetsapp.api.RetrofitInstance
import kotlinx.coroutines.runBlocking

class NavigationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        var result = makeApiRequestGetUser()
      //  var result = 1
        if (result == 1){
          //  Log.println(Log.DEBUG, "MYLOG", "true")
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
        }else if(result == 0){
           // Log.println(Log.DEBUG, "MYLOG", "false")
            val i = Intent(this, WebView::class.java)
            startActivity(i)
        }else{
            val i = Intent(this, WebViewEND::class.java)
            startActivity(i)
        }

        finish()
    }
    private fun makeApiRequestGetUser():Int {
        var type: Int = 0
        runBlocking{
            val response = RetrofitInstance.api.getUser(ApplicationClass.userData.deviceID)
            if (response.isSuccessful){
                var data = response.body()!!.get("user").asJsonObject
                ApplicationClass.userData.nickname = data.get("nickname").asString
                ApplicationClass.userData.name = data.get("name").asString
                ApplicationClass.userData.age = data.get("age").asInt
                ApplicationClass.userData.zodiac = data.get("zodiac").asString
                ApplicationClass.userData.meal_preferences = data.get("meal_preferences").asString
                ApplicationClass.userData.human_preferences = data.get("human_preferences").asString
                ApplicationClass.userData.photo = data.get("photo").asString
                ApplicationClass.userData.gender = data.get("gender").asString

                Log.d("MYLOG", "user-data updated successfully")
                type = 1
            }else{
                Log.e("MYLOG", "user does not exists")
            }
        }
        return type
    }
}