package com.siti.tugasbesarpemesanankopi2.User

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R

class ResetPassword : AppCompatActivity() {
    lateinit var send_email: EditText
    lateinit var btn_reset: Button

    lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        send_email = findViewById(R.id.send_email)
        btn_reset = findViewById(R.id.btn_reset)

        firebaseAuth = FirebaseAuth.getInstance()

        btn_reset.setOnClickListener {
            val email = send_email.text.toString()

            if (email == "") {
                Toast.makeText(this@ResetPassword, "All fileds are required!", Toast.LENGTH_SHORT).show()
            } else {
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this@ResetPassword, "Please check you Email", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@ResetPassword, LoginActivity::class.java))
                    } else {
                        val error = task.exception!!.message
                        Toast.makeText(this@ResetPassword, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

    }
}