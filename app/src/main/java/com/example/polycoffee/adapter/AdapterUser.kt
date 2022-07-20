package com.example.polycoffee.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.ItemUserBinding
import com.example.polycoffee.fragments.UserFragment
import com.example.polycoffee.model.User

class AdapterUser(var context: Context,var list:ArrayList<User>,var fragment:UserFragment) : RecyclerView.Adapter<AdapterUser.ViewHolder>() {
    class ViewHolder(binding:ItemUserBinding) : RecyclerView.ViewHolder(binding.root) {
        val img = binding.itemUserImg
        val username = binding.itemUserUsername
        val password = binding.itemUserPassword
        val hoTen = binding.itemUserHoTen
        val ngaySinh = binding.itemUserNgaySinh
        val diaChi = binding.itemUserDiaChi
        val sdt = binding.itemUserSdt
        val view = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val user = list[position]
        if(user.anhDaiDien!=""){
            holder.img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
        }
        holder.username.text = user.userName
        holder.password.text = user.passWord
        holder.hoTen.text = user.hoTen
        holder.ngaySinh.text = user.ngaySinh
        holder.diaChi.text = user.diaChi
        holder.sdt.text = user.sDT

        holder.view.setOnLongClickListener(object : View.OnLongClickListener{
            override fun onLongClick(p0: View?): Boolean {
                TempFunc.choosenDialog(context,user,fragment)
                return false
            }

        })
    }

    override fun getItemCount(): Int = list.size
}