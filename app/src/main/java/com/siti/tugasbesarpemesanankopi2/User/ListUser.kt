package com.siti.tugasbesarpemesanankopi2.User

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.siti.tugasbesarpemesanankopi2.Admin.Menu
import com.siti.tugasbesarpemesanankopi2.Model.User
import com.siti.tugasbesarpemesanankopi2.R
import com.siti.tugasbesarpemesanankopi2.User.Activity.CartUserActivity
import com.siti.tugasbesarpemesanankopi2.User.Activity.MainUserActivity
import kotlinx.android.synthetic.main.activity_list_user.*

class ListUser : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var databaseRef: DatabaseReference
    lateinit var menuList: MutableList<Menu>
    internal var firebaseUser: FirebaseUser? = null
    internal lateinit var reference: DatabaseReference




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_user)

        supportActionBar?.title = "User"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        recyclerView = this.findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        progressBar = findViewById(R.id.progressbar3)
        databaseRef = FirebaseDatabase.getInstance().getReference("Menu")


        // initialize mutable list
        menuList = mutableListOf()

        // call load data method in main thread
        LoadData()

        firebaseUser = FirebaseAuth.getInstance().currentUser
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser!!.uid)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java!!)
                tv_username.setText(user!!.username)
                if (user!!.imageUrl == "default") {
                    img_profile.setImageResource(R.mipmap.ic_launcher)
                } else {
                    //chage this
                    Glide.with(applicationContext).load(user!!.imageUrl).into(img_profile)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        })
    }

    private fun LoadData()
    {

        // show progress bar when call method as loading concept
        progressBar.visibility = View.VISIBLE

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error Encounter Due to "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {
                    //before fetch we have clear the list not to show duplicate value
                    menuList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Menu::class.java)
                        menuList.add(std!!)
                        val adapter = AdapterUser(menuList,this@ListUser )
                        recyclerView.adapter = adapter

                        progressBar.visibility = View.GONE
                        adapter.notifyDataSetChanged()
                    }
                }
                else
                {
                    // if no data found or you can check specefici child value exist or not here
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                    progressBar.visibility = View.GONE
                }

            }



        })

    }

    override fun onCreateOptionsMenu(menu: android.view.Menu?): Boolean {
        menuInflater.inflate(R.menu.menuusser, menu)
        return true
    }

    override fun onOptionsItemSelected (item: MenuItem?): Boolean {

        when(item?.itemId){
            R.id.itemcart ->{
                startActivity(
                    Intent(this@ListUser, CartUserActivity::class.java)
                )
            }
        }

        when(item?.itemId){
            R.id.itemhome ->{
                startActivity(
                    Intent(this@ListUser, MainUserActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }
}
