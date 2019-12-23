package com.siti.tugasbesarpemesanankopi2.Admin

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R
import com.siti.tugasbesarpemesanankopi2.User.Model.Order
import kotlinx.android.synthetic.main.activity_list_pesanan.*

class ListPesanan : AppCompatActivity() {
    lateinit var recyclerView: RecyclerView
    lateinit var databaseRef: DatabaseReference
    lateinit var orderList: MutableList<Order>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_pesanan)

        recyclerView = findViewById(R.id.recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        databaseRef = FirebaseDatabase.getInstance().getReference("Order")
        orderList = mutableListOf()
        LoadData()
    }

    private fun LoadData()
    {
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(databaseError: DatabaseError)
            {
                Toast.makeText(applicationContext,"Error Encounter Due to "+databaseError.message, Toast.LENGTH_LONG).show()/**/

            }

            override fun onDataChange(dataSnapshot: DataSnapshot)
            {
                if (dataSnapshot.exists())
                {//before fetch we have clear the list not to show duplicate value
                    orderList.clear()
                    // fetch data & add to list
                    for (data in dataSnapshot.children)
                    {
                        val std = data.getValue(Order::class.java)
                        orderList.add(std!!)
                    }

                    val adapter = OrderAdapter(orderList, this@ListPesanan)
                    recyclerview.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
                else
                {
                    // if no data found or you can check specefici child value exist or not here
                    Toast.makeText(applicationContext,"No data Found", Toast.LENGTH_LONG).show()
                }

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
                            this@ListPesanan, LoginActivity::
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
                    Intent(this@ListPesanan, MainAdminActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
