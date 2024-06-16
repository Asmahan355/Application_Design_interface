package com.example.appandroid.view

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.Data
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.appandroid.MainActivity
import com.example.appandroid.R
import com.example.appandroid.model.User
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.util.Date

class SetupProfileActivity : AppCompatActivity() {
    var auth: FirebaseAuth? = null
    var database: FirebaseDatabase? = null
    var storage: FirebaseStorage? = null
    var selectedImage: Uri? = null
    var dialog: ProgressDialog? = null
    var firebaseFireStore: FirebaseFirestore? = null
    private lateinit var imageView: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup_profile)

        dialog = ProgressDialog(this@SetupProfileActivity)
        dialog!!.setMessage("Updating Profile..")
        dialog!!.setCancelable(false)

        database = FirebaseDatabase.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        firebaseFireStore = FirebaseFirestore.getInstance()

        supportActionBar?.hide()

        imageView = findViewById<ImageView>(R.id.imageView)
        imageView.setOnClickListener {
            val intent = Intent()
            intent.action = Intent.ACTION_GET_CONTENT
            intent.type = "image/*"
            startActivityForResult(intent, 45)
        }

        val continueBtn2 = findViewById<Button>(R.id.continueBtn2)
        continueBtn2.setOnClickListener(View.OnClickListener {
            val name1 = findViewById<EditText>(R.id.nameBox)
            val name: String = name1.text.toString()

            if (name.isEmpty()) {
                name1.error = "Please type a Name"
                return@OnClickListener
            }

            dialog?.show()

            if (selectedImage != null) {
                val reference = storage!!.reference
                    .child("Profiles")
                    .child(auth!!.uid!!)

                reference.putFile(selectedImage!!)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            reference.downloadUrl
                                .addOnSuccessListener { uri ->
                                    val imageUri = uri.toString()
                                    val uid = auth!!.uid
                                    val phone = auth!!.currentUser!!.phoneNumber
                                    val user = User(uid, name, phone, imageUri)

                                    firebaseFireStore!!.collection("USERS")
                                        .add(user)
                                        .addOnCompleteListener { task ->
                                            if (task.isSuccessful) {
                                                dialog?.dismiss()
                                                val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                                                startActivity(intent)
                                                finish()
                                            } else {
                                                // Gérer l'erreur
                                                dialog?.dismiss()
                                                // Ajouter un message d'erreur ou un log
                                            }
                                        }
                                    database!!.reference
                                        .child("users")
                                        .child(uid!!)
                                        .setValue(user)
                                        .addOnSuccessListener {
                                            dialog!!.dismiss()
                                            val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                                            startActivity(intent)
                                            finish()
                                        }
                                }
                        }
                    }
            }
            else{
                val uid=auth!!.uid
                val phone=auth!!.currentUser!!.phoneNumber
                val user=User(uid,name,phone,"No Phone")
                firebaseFireStore!!.collection("USERS")
                    .add(user)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            dialog?.dismiss()
                            val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        } else {
                            // Gérer l'erreur
                            dialog?.dismiss()
                            // Ajouter un message d'erreur ou un log
                        }
                    }
                database!!.reference
                    .child("users")
                    .child(uid!!)
                    .setValue(user)
                    .addOnSuccessListener {
                        dialog!!.dismiss()
                        val intent = Intent(this@SetupProfileActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
            }
        })
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(data!=null)
        {
            if(data.data!=null)
            {
                val uri=data.data
                val storage=FirebaseStorage.getInstance()
                val time=Date().time
                val refrence=storage.reference
                    .child("Profiles")
                    .child(time.toString()+"")
                refrence.putFile(uri!!)
                    .addOnCompleteListener{ task->
                        if(task.isSuccessful){
                            refrence.downloadUrl.addOnSuccessListener { uri->
                                val filePath=uri.toString()
                                val obj=HashMap<String,Any>()
                                obj["image"]=filePath
                                database!!.reference.child("users")
                                    .child(FirebaseAuth.getInstance().uid!!)
                                    .updateChildren(obj)
                                    .addOnSuccessListener {  }
                            }
                        }

                    }


                imageView.setImageURI(data.data)
                selectedImage=data.data
            }
        }
    }
}