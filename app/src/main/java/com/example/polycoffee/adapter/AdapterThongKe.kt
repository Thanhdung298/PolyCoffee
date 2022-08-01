package com.example.polycoffee.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.databinding.DialogHoadonBinding
import com.example.polycoffee.databinding.ItemHoadonBinding
import com.example.polycoffee.databinding.ItemThongkeBinding
import com.example.polycoffee.model.HoaDon

class AdapterThongKe(var context: Context,var list:ArrayList<HoaDon>) : RecyclerView.Adapter<AdapterThongKe.ViewHolder>() {
    class ViewHolder(binding:ItemThongkeBinding) : RecyclerView.ViewHolder(binding.root) {
        val maHD = binding.itemThongkeMaHD
        val nguoiOrder = binding.itemThongkeNguoiOrder
        val maBan = binding.itemThongkeMaBan
        val ngay = binding.itemThongkeNgay
        val showHD = binding.itemThongkeShowHD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemThongkeBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val hoaDon = list[position]

        holder.maHD.text = "Mã hóa đơn: ${hoaDon.maHD}"
        holder.nguoiOrder.text = "Người order: ${hoaDon.userName}"
        holder.ngay.text = "Ngày tạo: ${hoaDon.ngay}"
        holder.maBan.text = "Bàn ${hoaDon.maBan}"

        holder.showHD.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val binding = DialogHoadonBinding.inflate(LayoutInflater.from(context))
            builder.setView(binding.root)
                .setPositiveButton("OK"){
                    p0,p1 -> p0.dismiss()
                }
            val alertDialog = builder.create()
            alertDialog.show()

            val recyclerView = binding.dialogHoadonRecyclerView
            val adapter = AdapterListSPTrongHD(context,hoaDon.listSP)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }
    }

    override fun getItemCount(): Int = list.size
}