package com.siti.tugasbesarpemesanankopi2.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.view.menu.MenuAdapter
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.model.ModelLoader
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R
import kotlinx.android.synthetic.main.activity_list_product.*


class ListProduct : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var databaseRef: DatabaseReference
    lateinit var menuList: MutableList<Menu>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_product)

        supportActionBar?.title = "List Product"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)



        recyclerView = this.findViewById(R.id.item_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        progressBar = findViewById(R.id.progressbar2)
        databaseRef = FirebaseDatabase.getInstance().getReference("Menu")


        // initialize mutable list
        menuList = mutableListOf()

        // call load data method in main thread
        LoadData()


        fab_add.setOnClickListener {
            startActivity(Intent(this, AddProduct::class.java))
        }


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
                            val adapter = com.siti.tugasbesarpemesanankopi2.Admin.MenuAdapter(menuList,this@ListProduct )
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
                            this@ListProduct, LoginActivity::
                            class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    )
                }
                setNegativeButton("Cancel") { _, _ -> }
            }
                .create().show()

        }

        when(item?.itemId){
            R.id.itemhomeadmin ->{
                startActivity(
                    Intent(this@ListProduct, MainAdminActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
