package com.example.polycoffee.dao

import android.content.Context
import android.widget.Toast
import com.example.polycoffee.model.*
import com.google.firebase.database.FirebaseDatabase


class DAO(private val context: Context) {
        fun insert(objectAny: Any,refName:String){
            val database = FirebaseDatabase.getInstance().getReference(refName)
            database.child(when(objectAny){
                is LoaiSanPham -> objectAny.maLoai
                is SanPham -> objectAny.maSP
                is Ban -> objectAny.maBan
                is User -> objectAny.userName
                is HoaDon -> objectAny.maHD
                else -> ""
            }).setValue(objectAny).addOnFailureListener { Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show() }
                .addOnSuccessListener { Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()}
        }
        fun remove(objectAny: Any,refName: String){
            val database = FirebaseDatabase.getInstance().getReference(refName)
            database.child(when(objectAny){
                is LoaiSanPham -> objectAny.maLoai
                is SanPham -> objectAny.maSP
                is Ban -> objectAny.maBan
                is User -> objectAny.userName
                is HoaDon -> objectAny.maHD
                else -> ""
            }).removeValue().addOnFailureListener { Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show() }
                .addOnSuccessListener { Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()}
        }


}