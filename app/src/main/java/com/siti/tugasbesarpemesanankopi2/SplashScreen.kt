package com.siti.tugasbesarpemesanankopi2

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val ivNgopi = findViewById<View>(R.id.img_splash) as ImageView
        val myanim = AnimationUtils.loadAnimation(this, R.anim.mytransition)
        val animationDrawable = relatif.getBackground() as AnimationDrawable

        // kodingan untuk membuat animasi backgroudnnya bergerak
        animationDrawable.setEnterFadeDuration(3000)
        animationDrawable.setExitFadeDuration(3000)
        animationDrawable.start()

        ivNgopi.startAnimation(myanim)
        val i = Intent(this, LoginActivity::class.java)
        val timer = object : Thread() {
            override fun run() {
                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {
                    startActivity(i)
                    finish()
                }
            }
        }
        timer.start()

    }
}
