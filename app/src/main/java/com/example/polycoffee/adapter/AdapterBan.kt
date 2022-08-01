package com.example.polycoffee.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
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
import com.example.polycoffee.model.HoaDon
import com.example.polycoffee.model.HoaDonTemp
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.ArrayList

class AdapterBan(val context: Context, val list:ArrayList<Ban>, val fragment:OrderFragment,var username:String="",var role:Int=0) : RecyclerView.Adapter<AdapterBan.ViewHolder>() {
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
        holder.id.text = "Bàn ${list[position].maBan.toInt()+1}"

        holder.hoaDon.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("Ban")
            database.child(ban.maBan).get().addOnSuccessListener{ snapshot ->
                    if (snapshot.hasChild("ListSP")){

                        val list = ArrayList<HoaDonTemp>()
                        for(snap in snapshot.child("ListSP").children){
                            list.add(snap.getValue(HoaDonTemp::class.java)!!)
                        }
                        val builder = AlertDialog.Builder(context)
                        val binding = DialogHoadonBinding.inflate(LayoutInflater.from(context))
                        builder.setView(binding.root)
                        if(role == 1){
                            builder.setPositiveButton("Đã thanh toán"){ p0,p1 ->

                                val databaseHD = FirebaseDatabase.getInstance().getReference("HoaDon")
                                databaseHD.get().addOnSuccessListener {
                                    val hoaDon = it.children.lastOrNull()?.getValue(HoaDon::class.java)
                                    val cal = Calendar.getInstance()
                                    val sdf = SimpleDateFormat("dd-MM-yyyy")
                                    if (hoaDon != null){
                                        val hoaDonSub = HoaDon(hoaDon.maHD+1,ban.maBan,username,list,sdf.format(cal.time))
                                        databaseHD.child(hoaDonSub.maHD.toString()).setValue(hoaDonSub).addOnSuccessListener {
                                            Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()
                                            database.child(ban.maBan).child("ListSP").removeValue()
                                            FirebaseDatabase.getInstance().getReference("Ban").child(ban.maBan).child("state").setValue("Trống")
                                        } .addOnFailureListener {
                                            Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show()
                                        }
                                    } else{
                                        val hoaDonSub = HoaDon(0,ban.maBan,username,list)
                                        databaseHD.child(hoaDonSub.maHD.toString()).setValue(hoaDonSub).addOnSuccessListener {
                                            Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()
                                            database.child(ban.maBan).child("ListSP").removeValue()
                                            FirebaseDatabase.getInstance().getReference("Ban").child(ban.maBan).child("state").setValue("Trống")
                                        } .addOnFailureListener {
                                            Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }
                                p0.dismiss()
                            }
                                .setNegativeButton("Hủy"){ p0,_ ->
                                    p0.dismiss()
                                }
                        } else{
                            builder.setPositiveButton("Hoàn thành"){
                                p0,p1 -> p0.dismiss()
                            }
                        }
                        val alertDialog = builder.create()
                        alertDialog.show()

                        val recyclerView = binding.dialogHoadonRecyclerView

                        val adapterHoaDon = AdapterHoaDon(context,list,ban.maBan)
                        recyclerView.layoutManager = LinearLayoutManager(context)
                        recyclerView.adapter = adapterHoaDon

                        adapterHoaDon.notifyDataSetChanged()
                    } else{
                        Toast.makeText(context,"Chưa order gì. Bảo khách gọi món đi",Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }


    override fun getItemCount(): Int = list.size

}