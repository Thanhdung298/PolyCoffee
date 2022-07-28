package com.example.polycoffee.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.OrderActivity
import com.example.polycoffee.databinding.DialogHoadonBinding
import com.example.polycoffee.databinding.DialogOrderBinding
import com.example.polycoffee.databinding.ItemBanBinding
import com.example.polycoffee.fragments.OrderFragment
import com.example.polycoffee.model.Ban
import com.example.polycoffee.model.HoaDonTemp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdapterBan(val context: Context, val list:ArrayList<Ban>, val fragment:OrderFragment) : RecyclerView.Adapter<AdapterBan.ViewHolder>() {
    class ViewHolder(binding:ItemBanBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.itemBanView
        val menu = binding.itemBanMenu
        val hoaDon = binding.itemBanHoaDon
        val id = binding.itemBanId
        val state = binding.itemBanState
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBanBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val ban = list[position]
        holder.menu.setOnClickListener {
            val intent = Intent(context,OrderActivity::class.java)
            intent.putExtra("maBan",ban.maBan)
            context.startActivity(intent)
        }

        holder.state.text = ban.state

        holder.hoaDon.setOnClickListener {

            val database = FirebaseDatabase.getInstance().getReference("Ban")
            database.child(ban.maBan).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.hasChild("ListSP")){
                        val builder = AlertDialog.Builder(context)
                        val binding = DialogHoadonBinding.inflate(LayoutInflater.from(context))
                        builder.setView(binding.root)
                            .setPositiveButton("Đã thanh toán"){ p0,p1 ->


                                database.child(ban.maBan).child("ListSP").removeValue()
//                                    .addOnSuccessListener {
//                                        Toast.makeText(context,"Thành công",Toast.LENGTH_SHORT).show()
//                                    } .addOnFailureListener {
//                                        Toast.makeText(context,"Thất bại",Toast.LENGTH_SHORT).show()
//                                    }

                                p0.dismiss()
                            }
                            .setNegativeButton("Hủy"){ p0,_ ->
                                p0.dismiss()
                            }
                        val alertDialog = builder.create()
                        alertDialog.show()

                        val recyclerView = binding.dialogHoadonRecyclerView
                        val list = ArrayList<HoaDonTemp>()
                        val adapterHoaDon = AdapterHoaDon(context,list)
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = adapterHoaDon
                        for(snap in snapshot.child("ListSP").children){
                            list.add(snap.getValue(HoaDonTemp::class.java)!!)
                        }
                        adapterHoaDon.notifyDataSetChanged()
                    } else{
                        Toast.makeText(context,"Chưa order gì. Bảo khách gọi món đi",Toast.LENGTH_SHORT).show()
                    }

                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        holder.id.text = "Bàn ${list[position].maBan.toInt()+1}"
    }

    override fun getItemCount(): Int = list.size
}