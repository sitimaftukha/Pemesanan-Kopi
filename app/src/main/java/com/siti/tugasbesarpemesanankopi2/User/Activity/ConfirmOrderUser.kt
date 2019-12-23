package com.siti.tugasbesarpemesanankopi2.User.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.siti.tugasbesarpemesanankopi2.Constant
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.Model.User
import com.siti.tugasbesarpemesanankopi2.R
import kotlinx.android.synthetic.main.activity_confirm_order_user.*



import java.util.HashMap

class ConfirmOrderUser : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_order_user)
        supportActionBar?.title = "User Confirm"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java!!)
                usertxt.setText(user!!.username)
//                if (user!!.imageUrl == "default") {
//                    img_profile.setImageResource(R.mipmap.ic_launcher)
//                } else {
//                    //chage this
//                    Glide.with(applicationContext).load(user!!.imageUrl).into(img_profile)
//                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })

        val totalPrice = intent.getStringExtra(Constant.KEY_TOTAL_CHART)

        btn_confirm_order.setOnClickListener {
            val nama = et_nama.text.toString()
            val no_telp = et_no_telp.text.toString()
            val alamat = et_alamat.text.toString()

            val id_user = FirebaseAuth.getInstance().currentUser?.uid

            val databaseOrder = FirebaseDatabase.getInstance().getReference("Order").child(id_user!!)
            val orderid = databaseOrder.key.toString()

            val hashMap = HashMap<String, String>()
            hashMap["user_id"] = id_user!!
            hashMap["orderid"] = orderid
            hashMap["name"] = nama
            hashMap["no_telp"] = no_telp
            hashMap["alamat"] = alamat
            hashMap["total"] = totalPrice

            databaseOrder.setValue(hashMap).addOnCompleteListener {

                val databaseref = FirebaseDatabase.getInstance().getReference("Chart").child(id_user)
                databaseref.removeValue()

                Toast.makeText(this, "Pesanan berhasil di submit", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainUserActivity::class.java))
                finish()
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menuusser, menu)
        return true
    }

    override fun onOptionsItemSelected (item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_logout) {
            AlertDialog.Builder(this).apply {
                setTitle("Apakah Anda akan keluar?")
                setPositiveButton("Ya") { _, _ ->
                    FirebaseAuth.getInstance().signOut()
                    startActivity(
                        Intent(
                            this@ConfirmOrderUser, LoginActivity::
                            class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                setNegativeButton("Cancel") { _, _ -> }
            }
                .create().show()

        }

        when(item?.itemId){
            R.id.itemhome ->{
                startActivity(
                    Intent(this@ConfirmOrderUser, MainUserActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }


}
