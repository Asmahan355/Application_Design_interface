package com.example.appandroid.view

import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.appandroid.MainActivity
import com.example.appandroid.R
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider

import java.util.concurrent.TimeUnit

class OtpActivity : AppCompatActivity() {

    var verificationId :String? = null
    var auth : FirebaseAuth? = null
    var dialog:ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_otp)
        FirebaseApp.initializeApp(this);

        dialog = ProgressDialog(this)
        dialog!!.setMessage("Sending OTP....")
        dialog!!.setCancelable(false)
        dialog!!.show()

        auth = FirebaseAuth.getInstance()
        supportActionBar?.hide()
        val phoneNumber = intent.getStringExtra("phoneNumber")
        val phoneLble=findViewById<TextView>(R.id.phoneLble)
        phoneLble.text = "Verify $phoneNumber"

        val option = PhoneAuthOptions.newBuilder(auth!!)
            .setPhoneNumber(phoneNumber!!)
            .setTimeout(60L,TimeUnit.SECONDS)
            .setActivity(this@OtpActivity)
            .setCallbacks(object
                :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
                override fun onVerificationCompleted(p0: PhoneAuthCredential) {}

                override fun onVerificationFailed(p0: FirebaseException) {}
                override fun onCodeSent(
                    verifyId: String,
                    forceResendingToken: PhoneAuthProvider.ForceResendingToken) {
                    super.onCodeSent(verifyId, forceResendingToken)
                    dialog!!.dismiss()
                    verificationId = verifyId
                    val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
                }
            }).build()
        PhoneAuthProvider.verifyPhoneNumber(option)
        val code1=findViewById<EditText>(R.id.code1)
        val code2=findViewById<EditText>(R.id.code2)
        val code3=findViewById<EditText>(R.id.code3)
        val code4=findViewById<EditText>(R.id.code4)
        val code5=findViewById<EditText>(R.id.code5)
        val code6=findViewById<EditText>(R.id.code6)
        val continueBtn01=findViewById<Button>(R.id.continueBtn01)
        continueBtn01.setOnClickListener {
            val code1 = code1.text.toString()
            val code2 = code2.text.toString()
            val code3 = code3.text.toString()
            val code4 = code4.text.toString()
            val code5 = code5.text.toString()
            val code6 = code6.text.toString()
            if (code1.trim().isEmpty()
                ||code2.trim().isEmpty()
                || code3.trim().isEmpty()
                ||code4.trim().isEmpty()
                ||code5.trim().isEmpty()
                || code6.trim().isEmpty()
            ){
                Toast.makeText(this,"Please Enter valid code",Toast.LENGTH_SHORT).show()
            }
            val otp = code1 +code2+code3+code4+code5+code6
            val credential = PhoneAuthProvider.getCredential(verificationId!!,otp)
            auth!!.signInWithCredential(credential)
                .addOnCompleteListener { task->
                    if (task.isSuccessful){
                        val intent = Intent(this, SetupProfileActivity::class.java)
                        startActivity(intent)
                        finishAffinity()
                    }
                    else{
                        Toast.makeText(this,"Failed",Toast.LENGTH_SHORT).show()
                    }
                }
        }

    }
}