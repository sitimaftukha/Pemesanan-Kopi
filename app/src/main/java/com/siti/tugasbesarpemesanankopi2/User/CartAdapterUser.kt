package com.siti.tugasbesarpemesanankopi2.User

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.siti.tugasbesarpemesanankopi2.R
import com.siti.tugasbesarpemesanankopi2.User.Activity.CartUserActivity
import com.siti.tugasbesarpemesanankopi2.User.Model.Chart

class CartAdapterUser (val cartList: List<Chart>, val context: Context) : RecyclerView.Adapter<CartAdapterUser .Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder
    {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chart, parent, false)
        return Holder(view)
    }

    override fun getItemCount(): Int
    {
        return cartList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int)
    {
        val id_user = FirebaseAuth.getInstance().currentUser?.uid
        val deletee = FirebaseDatabase.getInstance().getReference("Chart").child(id_user!!)


        holder.name.text = cartList[position].name
        holder.quantity.text = cartList[position].quantity
        holder.price.text = cartList[position].total

        holder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>("Delete")
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Pilihan Cart")

            builder.setItems(options, DialogInterface.OnClickListener{ dialog, which ->
//                if(which==0){
//                    val intent = Intent(context, CartUserActivity::class.java)
//                    intent.putExtra("edit", cartList[position].menuid)
//                    startActivity(intent)
//                }

                if (which == 0){
                    deletee.child(cartList[position].menuid).removeValue().addOnCompleteListener {
                        Toast.makeText(context, "Berhasil dihapus!!", Toast.LENGTH_SHORT).show()
                    }
                }
            })
            builder.show()
        }

    }




    // holder class
    class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val name = itemView.findViewById(R.id.tv_name) as TextView
        val quantity = itemView.findViewById(R.id.tv_quantity) as TextView
        val price = itemView.findViewById(R.id.tv_price) as TextView
    }
}