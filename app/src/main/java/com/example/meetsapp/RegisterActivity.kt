package com.example.meetsapp

import android.R.attr
import android.R.attr.*
import android.app.ProgressDialog
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.meetsapp.api.RetrofitInstance
import com.example.meetsapp.databinding.ActivityMainBinding
import com.example.meetsapp.databinding.ActivityRegisterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.runBlocking
import java.util.*
import kotlin.concurrent.schedule
import android.util.Patterns
import android.widget.Toast
import androidx.core.view.isVisible
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
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
                ApplicationClass.userData.photo = "images/"
                if (ImageUri != null){
                    uploadImage()
                }

                makeApiRequestPushUser()
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
//
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
        val ageInput: String = bindingClass.inputAge.text.toString().trim()
        return if (ageInput.isEmpty()) {
            bindingClass.inputAge.setError("Field can't be empty")
            false
        }else {
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

}