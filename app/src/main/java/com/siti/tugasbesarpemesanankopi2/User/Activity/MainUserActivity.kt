package com.siti.tugasbesarpemesanankopi2.User.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ViewFlipper
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R
import com.siti.tugasbesarpemesanankopi2.User.ListUser
import kotlinx.android.synthetic.main.activity_main_user.*



class MainUserActivity : AppCompatActivity() {

    private var promo = intArrayOf(R.drawable.store2, R.drawable.store3, R.drawable.store, R.drawable.store4, R.drawable.store5)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_user)

        supportActionBar?.title = "User"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        //flipper
        val viewFlipper = findViewById<ViewFlipper>(R.id.Flipper)
        if (viewFlipper != null) {
            viewFlipper.setInAnimation(applicationContext, android.R.anim.slide_in_left)
            viewFlipper.setOutAnimation(applicationContext, android.R.anim.slide_out_right)
        }

        if (viewFlipper != null) {
            for (image in promo) {
                val imageView = ImageView(this)
                val layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                layoutParams.gravity = Gravity.CENTER
                imageView.layoutParams = layoutParams
                imageView.setImageResource(image)
                viewFlipper.addView(imageView)
            }
        }


        //deklarasi animasi
        val animation2 = AnimationUtils.loadAnimation(this, R.anim.right)
        menu.startAnimation(animation2)
        val animation3 = AnimationUtils.loadAnimation(this, R.anim.left)
        cartt.startAnimation(animation3)

        menu.setOnClickListener {
            startActivity(Intent(this, ListUser::class.java))
        }

        cartt.setOnClickListener {
            startActivity(Intent(this, CartUserActivity::class.java))
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
                            this@MainUserActivity, LoginActivity::
                            class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                setNegativeButton("Cancel") { _, _ -> }
            }
                .create().show()

        }

        when(item?.itemId){
            R.id.itemcart ->{
                startActivity(
                    Intent(this@MainUserActivity, CartUserActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
