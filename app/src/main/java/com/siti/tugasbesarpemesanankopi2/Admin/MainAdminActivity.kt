package com.siti.tugasbesarpemesanankopi2.Admin

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.Model.User
import com.siti.tugasbesarpemesanankopi2.R
import com.siti.tugasbesarpemesanankopi2.User.Activity.MainUserActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*

class MainAdminActivity : AppCompatActivity() {

    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.title = "Admin"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)
//        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
//        setSupportActionBar(toolbar)
//
//        supportActionBar!!.setTitle("Admin")
//        toolbar.setLogo(R.drawable.ic_account)



        val animationDrawable = layoutmenu.getBackground() as AnimationDrawable

        // kodingan untuk membuat animasi backgroudnnya bergerak
        animationDrawable.setEnterFadeDuration(3000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        //deklarasi animasi
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        img_listmenu.startAnimation(animation2)
        img_addmenu.startAnimation(animation2)

        //deklarasi animasi
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.txt_anim)
        headertitle.startAnimation(animation3)


        img_listmenu.setOnClickListener {
            startActivity(Intent(this, ListPesanan::class.java))
        }

        img_addmenu.setOnClickListener {
            startActivity(Intent(this, ListProduct::class.java))
        }


        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java!!)
                useradmin.setText(user!!.username)
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



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.optionmenu, menu)
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
                            this@MainAdminActivity, LoginActivity::
                            class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                setNegativeButton("Cancel") { _, _ -> }
            }
                .create().show()

        }

        return super.onOptionsItemSelected(item)
    }

}
