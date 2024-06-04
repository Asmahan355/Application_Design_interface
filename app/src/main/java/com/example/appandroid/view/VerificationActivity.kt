package com.example.appandroid.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.appandroid.MainActivity
import com.example.appandroid.R
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class VerificationActivity : AppCompatActivity() {
    var auth:FirebaseAuth?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this);
        setContentView(R.layout.activity_verification)
        auth=FirebaseAuth.getInstance()
        if(auth!!.currentUser!=null)
        {
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        supportActionBar?.hide()
        val editNumber=findViewById<EditText>(R.id.editNumber)
        val continueBtn=findViewById<Button>(R.id.continueBtn)
        editNumber.requestFocus()
        continueBtn.setOnClickListener {
            val intent=Intent(this,OtpActivity::class.java)
            intent.putExtra("phoneNumber",editNumber.text.toString())
            startActivity(intent)
        }


    }
}