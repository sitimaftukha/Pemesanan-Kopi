package com.siti.tugasbesarpemesanankopi2.Admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.siti.tugasbesarpemesanankopi2.LoginActivity
import com.siti.tugasbesarpemesanankopi2.R

import java.util.*

class AddProduct : AppCompatActivity() {
    lateinit var menu_name: EditText
    lateinit var menu_price: EditText
    lateinit var add_image: ImageView
    lateinit var savebutton: Button
    lateinit var progressBar: ProgressBar


    private var imgPath: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)
        supportActionBar?.title = "Add Product"
        supportActionBar?.setLogo(R.drawable.ic_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(false)

        // below 2 line is for add seperator line in peritem of reyclerview
//        val itemDecor = DividerItemDecoration(this, VERTICAL)
//        recyclerView.addItemDecoration(itemDecor)

        menu_name = findViewById(R.id.editText);
        menu_price = findViewById(R.id.editText1);
        add_image = findViewById(R.id.img_product)
        savebutton = findViewById(R.id.savedata);
        progressBar = findViewById(R.id.progressbar)
//        databaseRef = FirebaseDatabase.getInstance().getReference("Menu")

        add_image.setOnClickListener {
            val mImg = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(mImg,0)
        }

//        // initialize mutable list
//        menuList = mutableListOf()


        // save data to database
        savebutton.setOnClickListener()
        {
            savedatatoserver()
            progressBar.visibility = View.VISIBLE
            startActivity(Intent(this, ListProduct::class.java))
            finish()
        }

//        // call load data method in main thread
//        ModelLoader.LoadData()
    }

    private fun savedatatoserver()
    {
        // get value from edit text & spinner
        val name: String = menu_name.text.toString().trim()
        val price: String = menu_price.text.toString().trim()

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(price))
        {
//            val menuid = databaseRef.push().key
//
//            val STD = Menu(menuid.toString(),name,price,image = "")
//            databaseRef.child(menuid.toString()).setValue(STD)
//
//            databaseRef.child(menuid.toString()).setValue(STD).addOnCompleteListener{
//
//                Toast.makeText(this,"Successfull", Toast.LENGTH_LONG).show()
//                progressBar.visibility = View.GONE
//                menu_image.visibility = View.GONE
//                menu_name.text = null
//                menu_price.text = null
//
//            }
            val filename = UUID.randomUUID().toString()
            val storageReference = FirebaseStorage.getInstance().getReference("image_product/$filename")
            val databaseReference = FirebaseDatabase.getInstance().getReference("Menu").push()
            val menuid = databaseReference.key

            storageReference.putFile(imgPath!!).addOnSuccessListener {
                storageReference.downloadUrl.addOnSuccessListener {
                    databaseReference.child("image").setValue(it.toString())
                    databaseReference.child("menuid").setValue(menuid.toString())
                    databaseReference.child("name").setValue(name)
                    databaseReference.child("price").setValue(price)


                    Toast.makeText(this, "Add product successfully", Toast.LENGTH_SHORT).show()
                    progressBar.visibility = View.GONE
                    add_image.visibility = View.GONE
                    menu_name.text = null
                    menu_price.text = null
                }
            }
                .addOnFailureListener {
                    println("Info File : ${it.message}")
                }


        }
        else
        {
            Toast.makeText(this,"Please Enter the name", Toast.LENGTH_LONG).show()
        }
    }



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            imgPath = data?.data

            try {
                //getting image from gallery
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, imgPath)

                //Setting image to ImageView
                add_image.setImageBitmap(bitmap)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
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
                            this@AddProduct, LoginActivity::
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
                    Intent(this@AddProduct, MainAdminActivity::class.java)
                )
            }
        }
        return super.onOptionsItemSelected(item)
    }

}
