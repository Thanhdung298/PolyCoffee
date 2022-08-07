package com.example.polycoffee.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.SubMenuActivity
import com.example.polycoffee.dao.FirebaseDatabaseTemp
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogOrderBinding
import com.example.polycoffee.databinding.ItemSpBinding
import com.example.polycoffee.model.HoaDonTemp
import com.example.polycoffee.model.SanPham
import com.google.firebase.database.FirebaseDatabase

class AdapterSP(val context: Context, val list:ArrayList<SanPham>,val type:Int,var maBan:String="", val acitivty:SubMenuActivity= SubMenuActivity()) : RecyclerView.Adapter<AdapterSP.ViewHolder>() {
    class ViewHolder(binding:ItemSpBinding) : RecyclerView.ViewHolder(binding.root) {
        val view = binding.itemSpView
        val tenSP = binding.itemSpTenSP
        val gia = binding.itemSpGia
        val img = binding.itemSpImg
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSpBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val sanPham = list[position]
        holder.tenSP.text = sanPham.tenSP
        holder.gia.text = "${sanPham.giaSP} VND"
        if(sanPham.img!=""){
            holder.img.setImageBitmap(TempFunc.StringToBitmap(sanPham.img))
        }
        if(type==0){
            holder.view.setOnLongClickListener(object : View.OnLongClickListener{
                override fun onLongClick(v: View?): Boolean {
                    TempFunc.choosenDialog(context,sanPham,acitivty,"SanPham")
                    return false
                }

            })
        }
        if(type==1){ //type = 1 la order
            holder.view.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val binding = DialogOrderBinding.inflate(LayoutInflater.from(context))
                builder.setView(binding.root)

                val alertDialog = builder.create()
                alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                alertDialog.show()

                val img = binding.dialogOrderImg
                val tenSP = binding.dialogOrderTenSP
                val soLuongOrder = binding.dialogOrderNumber
                val saveBtn = binding.dialogOrderSaveBtn

                if(sanPham.img!=""){
                    img.setImageBitmap(TempFunc.StringToBitmap(sanPham.img))
                }

                tenSP.text = sanPham.tenSP
                saveBtn.setOnClickListener {
                    if(soLuongOrder.number.toString().toInt() == 0){
                        Toast.makeText(context,"Số lượng phải lớn hơn 0",Toast.LENGTH_SHORT).show()
                    } else{
                        val database = FirebaseDatabaseTemp.getDatabase()!!.getReference("Ban").child(maBan)
                        database.child("ListSP").child(sanPham.maSP.toString()).setValue(HoaDonTemp(sanPham.maSP,sanPham.tenSP,soLuongOrder.number.toString().toInt(),sanPham.giaSP)).addOnSuccessListener {
                            Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()
                            alertDialog.dismiss()
                            database.child("state").setValue("Chưa thanh toán")
                        } .addOnFailureListener {
                            Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

    

    override fun getItemCount(): Int = list.size
}