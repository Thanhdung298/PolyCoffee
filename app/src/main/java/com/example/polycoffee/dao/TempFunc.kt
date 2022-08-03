package com.example.polycoffee.dao

import android.app.AlertDialog
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.view.LayoutInflater
import android.widget.EditText
import com.example.polycoffee.SubMenuActivity
import com.example.polycoffee.databinding.DialogChoosenBinding
import com.example.polycoffee.fragments.MenuFragment
import com.example.polycoffee.fragments.UserFragment
import com.example.polycoffee.model.LoaiSanPham
import com.example.polycoffee.model.SanPham
import com.example.polycoffee.model.User
import com.google.android.material.textfield.TextInputLayout
import java.io.ByteArrayOutputStream

class TempFunc {
    companion object{
        fun BitMapToString(bitmap: Bitmap):String{
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos)
            val b = baos.toByteArray()
            return Base64.encodeToString(b,Base64.DEFAULT)
        }
        fun StringToBitmap(imgStr:String):Bitmap{
            val imgBytes = Base64.decode(imgStr,0)
            return BitmapFactory.decodeByteArray(imgBytes,0,imgBytes.size)
        }
        fun choosenDialog(context:Context,objectAny: Any,fragmentAny: Any,refName:String){
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
                    ) { p0, _ ->
                        p0.dismiss()
                    }.setPositiveButton("Chắc chắn"){
                            p0, _ ->
                        DAO(context).remove(objectAny,refName)
//                        when(fragmentAny){
//                            is MenuFragment -> fragmentAny.getListLSP()
//                            is UserFragment -> fragmentAny.getListLSP()
//                        }
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
                    is SubMenuActivity -> fragmentAny.openDialogSP(objectAny as SanPham,1,context)
                }
            }

        }

        fun checkField(vararg check: TextInputLayout){
            for (view in check){
                if(view.editText!!.text.isEmpty()){
                    view.error = "Bạn phải nhập vào trường này"
                } else view.error = null
            }
        }

        fun noError(vararg check: TextInputLayout):Boolean{
            for (view in check){
                if (view.error!=null) return false
            }
            return true
        }

        fun checkNumber(check: TextInputLayout):Boolean{
            try {
                if (check.editText!!.text.toString().toInt()<=0){
                    check.error = "Giá phải là số lớn hơn 0"
                    return false
                }
            } catch (e:Exception){
                check.error = "Giá phải là số lớn hơn 0"
                e.printStackTrace()
                return false
            }
            return true
        }

        fun resetField(vararg field: EditText){
            for(i in field){
                i.text = null
            }
            field[0].requestFocus()
        }
    }
}