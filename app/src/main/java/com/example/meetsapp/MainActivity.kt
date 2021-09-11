package com.example.meetsapp

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.DiffUtil.DiffResult
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.example.meetsapp.api.RetrofitInstance
import com.example.meetsapp.model.ItemModel
import com.yuyakaido.android.cardstackview.*
import kotlinx.coroutines.runBlocking
import java.util.ArrayList

//
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import com.example.meetsapp.databinding.ActivityMainBinding
//
//class MainActivity : AppCompatActivity() {
//    lateinit var bindingClass : ActivityMainBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        bindingClass = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(bindingClass.root)
//
//    }
//}
class MainActivity : AppCompatActivity() {
    private var manager: CardStackLayoutManager? = null
    private var adapter: CardStackAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chat = findViewById<ImageButton>(R.id.commentbtn)
        chat.setOnClickListener{
            chat()
        }
        val cardStackView = findViewById<CardStackView>(R.id.card_stack_view)
        manager = CardStackLayoutManager(this, object : CardStackListener {
            override fun onCardDragging(direction: Direction, ratio: Float) {
                Log.d(TAG, "onCardDragging: d=" + direction.name + " ratio=" + ratio)
            }

            override fun onCardSwiped(direction: Direction) {
                Log.d(TAG, "onCardSwiped: p=" + manager!!.topPosition + " d=" + direction)
                if (direction == Direction.Right) {
                    Toast.makeText(this@MainActivity, "Direction Right", Toast.LENGTH_SHORT).show()
                    chat()
                }
                if (direction == Direction.Top) {
                    Toast.makeText(this@MainActivity, "Direction Top", Toast.LENGTH_SHORT).show()
                }
                if (direction == Direction.Left) {
                    Toast.makeText(this@MainActivity, "Direction Left", Toast.LENGTH_SHORT).show()
                    chat()
                }
                if (direction == Direction.Bottom) {
                    Toast.makeText(this@MainActivity, "Direction Bottom", Toast.LENGTH_SHORT).show()
                }

                // Paginating
                if (manager!!.topPosition == adapter!!.itemCount - 5) {
                    paginate()
                }
            }

            override fun onCardRewound() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardCanceled() {
                Log.d(TAG, "onCardRewound: " + manager!!.topPosition)
            }

            override fun onCardAppeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }

            override fun onCardDisappeared(view: View, position: Int) {
                val tv = view.findViewById<TextView>(R.id.item_name)
                Log.d(TAG, "onCardAppeared: " + position + ", nama: " + tv.text)
            }
        })
        manager!!.setStackFrom(StackFrom.None)
        manager!!.setVisibleCount(3)
        manager!!.setTranslationInterval(8.0f)
        manager!!.setScaleInterval(0.95f)
        manager!!.setSwipeThreshold(0.3f)
        manager!!.setMaxDegree(20.0f)
        manager!!.setDirections(Direction.FREEDOM)
        manager!!.setCanScrollHorizontal(true)
        manager!!.setSwipeableMethod(SwipeableMethod.Manual)
        manager!!.setOverlayInterpolator(LinearInterpolator())
        adapter = CardStackAdapter(addList())
        cardStackView.layoutManager = manager
        cardStackView.adapter = adapter
        cardStackView.itemAnimator = DefaultItemAnimator()
    }

    private fun paginate() {
        val old: List<ItemModel> = adapter!!.items
        val baru: List<ItemModel> = ArrayList(addList())
        val callback = CardStackCallback(old, baru)
        val hasil = DiffUtil.calculateDiff(callback)
        adapter!!.items = baru
        hasil.dispatchUpdatesTo(adapter!!)
    }

    private fun addList(): List<ItemModel> {
        val items: MutableList<ItemModel> = ArrayList<ItemModel>()
        var i = 0
        while (i<10){
            items.add(makeApiRequestGetPair())
            i++
        }
//        items.add(ItemModel(R.drawable.sample1, "Markonah", "24", "Jember"))
//        items.add(ItemModel(R.drawable.sample2, "Marpuah", "20", "Malang"))
//        items.add(ItemModel(R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel(R.drawable.sample4, "Markobar", "18", "Bandung"))
//        items.add(ItemModel(R.drawable.sample5, "Marmut", "19", "Hutan"))
//        items.add(ItemModel(R.drawable.sample1, "Markonah", "24", "Jember"))
//        items.add(ItemModel(R.drawable.sample2, "Marpuah", "20", "Malang"))
//        items.add(ItemModel(R.drawable.sample3, "Sukijah", "27", "Jonggol"))
//        items.add(ItemModel(R.drawable.sample4, "Markobar", "19", "Bandung"))
//        items.add(ItemModel(R.drawable.sample5, "Marmut", "15", "Hutan"))
        return items
    }
    private fun makeApiRequestGetPair():ItemModel {
        var pair: ItemModel = ItemModel("","","")
        runBlocking{
            val response = RetrofitInstance.api.getPair(ApplicationClass.userData.deviceID)
            if (response.isSuccessful){
                var data = response.body()!!.get("pair").asJsonObject
                pair =ItemModel(
                    data.get("name").asString,
                    data.get("age").asString,
                    data.get("photo").asString,
                )
                ApplicationClass.userData.name = data.get("name").asString
              //  Log.d("MYLOG", "pair updated successfully")
            }else{
                Log.e("MYLOG", "pair does not exists")
            }
        }
        return pair
    }
    companion object {
        private const val TAG = "MainActivity"
    }

    fun dislike(view: View) {
        return
    }
    fun chat(){
         val UID = "SUPERHERO1" // Replace with the UID of the user to login
         val AUTH_KEY = "653632456a2c3892b8a87e31a49df72e4b5add69" // Replace with your App Auth Key
//        Log.d("MYLOG", "Login Successful : ")
        CometChat.login(UID, AUTH_KEY, object : CometChat.CallbackListener<User?>() {
            override fun onSuccess(user: User?) {
                Log.d(TAG, "Login Successful : "+user.toString())
            }

            override fun onError(e: CometChatException) {
                Log.d(TAG, "Login failed with exception: " + e.message);
            }
        })
    }

}