package com.example.polycoffee.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton
import com.example.polycoffee.databinding.ItemHoadonBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.model.HoaDonTemp
import com.google.firebase.database.FirebaseDatabase

class AdapterHoaDon(val context: Context, val list:ArrayList<HoaDonTemp>,val maBan:String = "") : RecyclerView.Adapter<AdapterHoaDon.ViewHolder>() {
    class ViewHolder(binding:ItemHoadonBinding) : RecyclerView.ViewHolder(binding.root) {
        val tenSP = binding.itemHdTenSP
        val soLuong = binding.itemHdSoLuong
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemHoadonBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hoaDon = list[position]
        holder.tenSP.text = hoaDon.tenSP
        holder.soLuong.number = hoaDon.soLuong.toString()
        holder.soLuong.setOnValueChangeListener(object : ElegantNumberButton.OnValueChangeListener{
            override fun onValueChange(view: ElegantNumberButton?, oldValue: Int, newValue: Int) {
                val database = FirebaseDatabase.getInstance().getReference("Ban")
                database.child(maBan).child("ListSP").child(hoaDon.maSP).child("soLuong").setValue(newValue).addOnSuccessListener {
                    Log.d("update soLuong","Thanh cong")
                } .addOnFailureListener {
                    Log.d("update soLuong","That bai")
                }
            }

        })
    }

    override fun getItemCount(): Int = list.size
}