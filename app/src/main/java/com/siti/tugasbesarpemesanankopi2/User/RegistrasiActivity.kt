package com.siti.tugasbesarpemesanankopi2.User

import android.app.ProgressDialog
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R
import kotlinx.android.synthetic.main.activity_register.*
import java.util.HashMap

class RegistrasiActivity : AppCompatActivity() {


    internal lateinit var username: EditText
    internal lateinit var fullname: EditText
    internal lateinit var email: EditText
    internal lateinit var password: EditText
    internal lateinit var register: Button

    internal lateinit var auth: FirebaseAuth
    internal lateinit var reference: DatabaseReference
    internal lateinit var pd: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)



        val animationDrawable = relatif2.getBackground() as AnimationDrawable

        // kodingan untuk membuat animasi backgroudnnya bergerak
        animationDrawable.setEnterFadeDuration(3000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()


        btnCancel.setOnClickListener {
            startActivity(Intent(this@RegistrasiActivity, LoginActivity::class.java))
        }

        username = findViewById(R.id.username)
        email = findViewById(R.id.edit_email)
        fullname = findViewById(R.id.fullname)
        password = findViewById(R.id.edit_password)
        register = findViewById(R.id.btnRegister)
        auth = FirebaseAuth.getInstance()

        fun emailValidation():Boolean{
            val email = email.text.toString()
            val pattern = Patterns.EMAIL_ADDRESS
            return pattern.matcher(email).matches()
        }
        register.setOnClickListener {


            val str_username = username.text.toString()
            val str_fullname = fullname.text.toString()
            val str_email = email.text.toString()
            val str_password = password.text.toString()
            val isEmailValid = emailValidation()
            println("EMAIL IS VALID ? " + isEmailValid)

            if (TextUtils.isEmpty(str_fullname)) {
                Toast.makeText(this@RegistrasiActivity, "Nama harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_username)) {
                Toast.makeText(this@RegistrasiActivity, "Username harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(str_email)) {
                Toast.makeText(this@RegistrasiActivity, "Email harus diisi!", Toast.LENGTH_SHORT).show()
            } else if(isEmailValid != true){
                Toast.makeText(this@RegistrasiActivity, "Please input your email Correctly", Toast.LENGTH_SHORT).show()
            }else if (TextUtils.isEmpty(str_password)) {
                Toast.makeText(this@RegistrasiActivity, "Password harus diisi!", Toast.LENGTH_SHORT).show()
            } else if (str_password.length < 6) {
                Toast.makeText(this@RegistrasiActivity, "Password harus 6 characters!", Toast.LENGTH_SHORT).show()
            } else {
                pd = ProgressDialog(this@RegistrasiActivity)
                pd.setMessage("Mohon tunggu...")
                pd.show()
                register(str_username, str_fullname, str_email, str_password)
            }
        }
    }

    fun register(username: String, fullname: String, email: String, password: String) {
        println("USERNAME : " + username)
        println("FULLNAME :" + fullname)
        println("EMAIL :" +  email)
        println("PASSWORD : " + password)
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this@RegistrasiActivity) { task ->
                if (task.isSuccessful) {
                    val firebaseUser = auth.currentUser
                    val userID = firebaseUser!!.uid

                    reference = FirebaseDatabase.getInstance().reference.child("Users").child(userID)
                    val map = HashMap<String, Any>()
                    map["id"] = userID
                    map["username"] = username.toLowerCase()
                    map["fullname"] = fullname
                    map["bio"] = ""

                    reference.setValue(map).addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            pd.dismiss()
                            val intent = Intent(this@RegistrasiActivity, LoginActivity::class.java)
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                            startActivity(intent)
                        }
                    }
                } else {
                    pd.dismiss()
                    Toast.makeText(this@RegistrasiActivity, "Email and password already registered !", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
