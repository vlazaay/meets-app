package com.example.meetsapp


import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType.TYPE_CLASS_NUMBER
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.cometchat.pro.core.CometChat
import com.cometchat.pro.exceptions.CometChatException
import com.cometchat.pro.models.User
import com.example.meetsapp.api.RetrofitInstance
import com.example.meetsapp.databinding.ActivityRegisterBinding
import com.example.meetsapp.utils.Constants.Companion.API_KEY_CHAT
import kotlinx.coroutines.runBlocking
import java.util.*
import com.google.firebase.storage.FirebaseStorage
import com.onesignal.OneSignal
import java.text.SimpleDateFormat


class RegisterActivity : AppCompatActivity() {
    lateinit var bindingClass: ActivityRegisterBinding
    var ImageUri : Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)
        bindingClass.btnUpload.setOnClickListener{
            selectImage()
        }
        val options = arrayOf("male", "female")
        bindingClass.gender.adapter = ArrayAdapter<String>(this, R.layout.spinner, options)
        bindingClass.gender.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                ApplicationClass.userData.gender = options.get(p2)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }
        bindingClass.btnRegister.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                if (!validateNickname() or !validateName() or !validateAge() or !validateZodiac() or !validateMealPreferences() or !validateHumanPreferences()){
                    return
                }
                ApplicationClass.userData.nickname = bindingClass.inputNickname.text.toString()
                ApplicationClass.userData.name = bindingClass.inputName.text.toString()
                ApplicationClass.userData.age = bindingClass.inputAge.text.toString().toInt()
                ApplicationClass.userData.zodiac = bindingClass.inputZodiac.text.toString()
                ApplicationClass.userData.meal_preferences = bindingClass.mealPreferences.text.toString()
                ApplicationClass.userData.human_preferences = bindingClass.humanPreferences.text.toString()
                if (ImageUri != null){
                    uploadImage()
                }
                val device = OneSignal.getDeviceState()
                if (device != null) {
                    if (ApplicationClass.userData.deviceID != device.userId) {
                        ApplicationClass.userData.deviceID = device.userId
                    }
                }
                makeApiRequestPushUser()
                createUserChat()
                goNavigation()
            }
        })
    }
    private fun uploadImage(){
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading File ...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val formatter = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault())
        val now = Date()
        val filename = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("images/$filename")
        ApplicationClass.userData.photo = "images/$filename"
        storageReference.putFile(ImageUri!!).
        addOnSuccessListener {
//                Toast.makeText(applicationContext, "Successfuly uploaded", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()

        }.addOnFailureListener{
            bindingClass.message.text = "Failed"
            if (progressDialog.isShowing) progressDialog.dismiss()
//                Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
    private fun selectImage(){
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100 && resultCode == RESULT_OK){
            ImageUri = data?.data!!
            bindingClass.firebaseImage.setImageURI(ImageUri)
            val userfilename: String = ImageUri!!.getLastPathSegment().toString()
            bindingClass.message.text = userfilename
        }
    }
    fun goNavigation() {
        val i = Intent(this, NavigationActivity::class.java)
        startActivity(i)
    }
    private fun makeApiRequestPushUser() {
        runBlocking{
            try {
                val response = RetrofitInstance.api.pushUser(ApplicationClass.userData)
                Log.d("MYLOG", response.raw().toString())
            } catch (e: Exception) {
                Log.e("MYLOG", "Error: ${e.message}")
            }
        }
    }
//    private fun validateEmail(): Boolean {
//        val emailInput: String = textInputEmail.getEditText().getText().toString().trim()
//        return if (emailInput.isEmpty()) {
//            textInputEmail.setError("Field can't be empty")
//            false
//        } else if (!Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
//            textInputEmail.setError("Please enter a valid email address")
//            false
//        } else {
//            textInputEmail.setError(null)
//            true
//        }
//    }

    private fun validateNickname(): Boolean {
        val usernameInput: String = bindingClass.inputNickname.text.toString().trim()
        return if (usernameInput.isEmpty()) {
            bindingClass.inputNickname.setError("Field can't be empty")
            false
        } else if (usernameInput.length > 15) {
            bindingClass.inputNickname.setError("Username too long")
            false
        } else {
            bindingClass.inputNickname.setError(null)
            true
        }
    }
    private fun validateName(): Boolean {
        val nameInput: String = bindingClass.inputName.text.toString().trim()
        return if (nameInput.isEmpty()) {
            bindingClass.inputName.setError("Field can't be empty")
            false
        } else if (nameInput.length > 15) {
            bindingClass.inputName.setError("Name too long")
            false
        } else {
            bindingClass.inputName.setError(null)
            true
        }
    }
    private fun validateAge(): Boolean {
        val ageInput: String = bindingClass.inputAge.text.toString()
        return if (ageInput.isEmpty()) {
            bindingClass.inputAge.setError("Field can't be empty")
            false
        }else if (ageInput.toInt() < 16) {
            bindingClass.inputAge.setError("Age must be more than 16")
            false
        }else{
            bindingClass.inputAge.setError(null)
            true
        }
    }
    private fun validateZodiac(): Boolean {
        val zodiacInput: String = bindingClass.inputZodiac.text.toString().trim()
        return if (zodiacInput.isEmpty()) {
            bindingClass.inputZodiac.setError("Field can't be empty")
            false
        } else if (zodiacInput.length > 15) {
            bindingClass.inputZodiac.setError("Input too long")
            false
        } else {
            bindingClass.inputZodiac.setError(null)
            true
        }
    }
    private fun validateMealPreferences(): Boolean {
        val mealPreferences: String = bindingClass.mealPreferences.text.toString().trim()
        return if (mealPreferences.isEmpty()) {
            bindingClass.mealPreferences.setError("Field can't be empty")
            false
        } else if (mealPreferences.length > 255) {
            bindingClass.mealPreferences.setError("Input too long")
            false
        } else {
            bindingClass.mealPreferences.setError(null)
            true
        }
    }
    private fun validateHumanPreferences(): Boolean {
        val humanPreferences: String = bindingClass.humanPreferences.text.toString().trim()
        return if (humanPreferences.isEmpty()) {
            bindingClass.humanPreferences.setError("Field can't be empty")
            false
        } else if (humanPreferences.length > 255) {
            bindingClass.humanPreferences.setError("Name too long")
            false
        } else {
            bindingClass.humanPreferences.setError(null)
            true
        }
    }

    fun createUserChat(){
        val apiKey = API_KEY_CHAT // Replace with your API Key.
        val user = User()
        user.uid = ApplicationClass.userData.deviceID // Replace with your uid for the user to be created.
        user.name = ApplicationClass.userData.name // Replace with the name of the user

        CometChat.createUser(user, apiKey, object : CometChat.CallbackListener<User>() {
            override fun onSuccess(user: User) {
                Log.d("MYLOG", user.toString())
            }

            override fun onError(e: CometChatException) {
                Log.e("MYLOG", e.message.toString())
            }
        })
    }

}