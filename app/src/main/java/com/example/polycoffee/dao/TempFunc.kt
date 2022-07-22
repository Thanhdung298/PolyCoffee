package com.example.polycoffee.dao

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import com.example.polycoffee.databinding.DialogChoosenBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.fragments.UserFragment
import com.example.polycoffee.model.LoaiSanPham
import com.example.polycoffee.model.User
import java.io.ByteArrayOutputStream
import java.util.*

class TempFunc {
    companion object{
        fun BitMapToString(bitmap: Bitmap):String{
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.PNG,100,baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b,Base64.DEFAULT)
        }
        fun StringToBitmap(imgStr:String):Bitmap{
            val imgBytes = Base64.decode(imgStr,0)
            return BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.size)
        }
        fun choosenDialog(context:Context,objectAny: Any,fragmentAny: Any){
            val builder = AlertDialog.Builder(context)
            val binding = DialogChoosenBinding.inflate(LayoutInflater.from(context))
            builder.setView(binding.root)
                .setTitle("Chọn chức năng")
            val alertDialog = builder.create()
            alertDialog.show()

            val removeBtn = binding.removeBtn
            val editBtn = binding.editBtn
            removeBtn.setOnClickListener{
                alertDialog.dismiss()
                val builderRemove = AlertDialog.Builder(context)
                    .setTitle("Xóa")
                    .setMessage("Bạn chắc chắn xóa chứ?")
                    .setNegativeButton("Cancel"
                    ) { p0, p1 ->
                        p0.dismiss()
                    }.setPositiveButton("Chắc chắn"){
                            p0,p1 ->
                        DAO(context).remove(objectAny,"LoaiSP")
                        when(fragmentAny){
                            is MenuFragment -> fragmentAny.getListLSP()
                            is UserFragment -> fragmentAny.getListLSP()
                        }
                        p0.dismiss()
                    }
                val dialog = builderRemove.create()
                dialog.show()
            }
            editBtn.setOnClickListener {
                alertDialog.dismiss()
                when(fragmentAny){
                    is MenuFragment ->  fragmentAny.openDialogLSP(objectAny as LoaiSanPham,1)
                    is UserFragment -> fragmentAny.openDialog(objectAny as User,1)
                }
            }

        }
    }
}