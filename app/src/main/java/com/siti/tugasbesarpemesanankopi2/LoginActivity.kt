package com.siti.tugasbesarpemesanankopi2

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.siti.tugasbesarpemesanankopi2.Admin.MainAdminActivity
import com.siti.tugasbesarpemesanankopi2.User.Activity.MainUserActivity
import com.siti.tugasbesarpemesanankopi2.User.RegistrasiActivity
import com.siti.tugasbesarpemesanankopi2.User.ResetPassword
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    internal lateinit var email: EditText
    internal lateinit var password: EditText
    internal lateinit var login: Button
    internal lateinit var txt_forgot: TextView

    internal var firebaseUser: FirebaseUser? = null

    internal lateinit var auth: FirebaseAuth

//    override fun onStart() {
//        super.onStart()
//        firebaseUser = FirebaseAuth.getInstance().currentUser
//
//        if (firebaseUser != null) {
//            val intent = Intent(this@LoginActivity, MainActivity::class.java)
//            startActivity(intent)
//            finish()
//        }
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)



        email = findViewById(R.id.email)
        password = findViewById(R.id.password)

        email.clearFocus()
        password.clearFocus()

        login = findViewById(R.id.btnLogin)
        txt_forgot = findViewById(R.id.txt_forgot)

        auth = FirebaseAuth.getInstance()

        txt_signup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegistrasiActivity::class.java))
        }

        txt_forgot.setOnClickListener {
            val intentMessage = Intent(this@LoginActivity, ResetPassword::class.java)
            startActivity(intentMessage)
        }

        login.setOnClickListener {

            val str_email = email.text.toString()
            val str_password = password.text.toString()
            //CHECK THE CONNECTION ON DEVICE
            val connectivityManager = this@LoginActivity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
            val isConnectivityManager: Boolean = activeNetwork?.isConnected == true




            if (TextUtils.isEmpty(str_email) || TextUtils.isEmpty(str_password)) {
                Toast.makeText(this@LoginActivity, "All fields are required!", Toast.LENGTH_SHORT).show()
            } else if (isConnectivityManager) {
//                val pd = ProgressDialog(this@LoginActivity)
//                pd.setMessage("Please wait...")
//                pd.show()
                auth.signInWithEmailAndPassword(str_email, str_password)
                    .addOnCompleteListener(this@LoginActivity) { task ->
                        if (task.isSuccessful) {

                            if(str_email == "sitimaftukha@gmail.com"){
                                val intent = Intent(this@LoginActivity, MainAdminActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }else{
                                val intent = Intent(this@LoginActivity, MainUserActivity::class.java)
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                                startActivity(intent)
                                finish()
                            }

//                            val reference = FirebaseDatabase.getInstance().reference.child("Users")
//                                .child(auth.currentUser!!.uid)
//
//                            reference.addValueEventListener(object : ValueEventListener {
//                                override fun onDataChange(dataSnapshot: DataSnapshot) {
////                                        pd.dismiss()
//                                    val intent = Intent(this@LoginActivity, MainUserActivity::class.java)
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
//                                    startActivity(intent)
//                                    finish()
//                                }
//
//                                override fun onCancelled(databaseError: DatabaseError) {
////                                        pd.dismiss()
//                                }
//                            })
                        } else {
//                                pd.dismiss()
                            Toast.makeText(this@LoginActivity, "Username salah", Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                showDialogInternet()
            }
        }
    }

    fun showDialogInternet() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Network Error")
            .setMessage("Please check your internet connection and try again")
            .setPositiveButton("OK", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            .setCancelable(true)
        builder.show()
    }
}
