package com.example.polycoffee

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterMenu
import com.example.polycoffee.adapter.AdapterSP
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.ActivitySubMenuBinding
import com.example.polycoffee.databinding.DialogSanphamBinding
import com.example.polycoffee.model.LoaiSanPham
import com.example.polycoffee.model.SanPham
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage

class SubMenuActivity : AppCompatActivity() {
    lateinit var binding:ActivitySubMenuBinding
    var bitmapSP:Bitmap? = null
    var listSP = ArrayList<SanPham>()
    lateinit var img:ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var adapterSP: AdapterSP

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.subMenuFab.setOnClickListener {
            openDialogSP(SanPham(),0)
        }
        updateRecyclerView()
        getListLSP(listSP,adapterSP)
    }

    fun getListLSP(list:ArrayList<SanPham>,adapterSP: AdapterSP){
        val database = FirebaseDatabase.getInstance().getReference("SanPham")
        val maLoai = intent.getStringExtra("maLoai").toString()
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                list.clear()
                for (datasnap in snapshot.children){
                    val sanPham = datasnap.getValue(SanPham::class.java)
                    if (sanPham != null) {
                        if(sanPham.maLoai==maLoai){
                            list.add(sanPham)
                        }
                    }
                }
                list.sortWith(compareBy { it.maLoai })
                adapterSP.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SubMenuActivity,"Failed", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun updateRecyclerView(){
        listSP = ArrayList()
        recyclerView = binding.subMenuRecyclerView
        adapterSP = AdapterSP(this,listSP)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterSP
    }

    fun openDialogSP(sanPham: SanPham, type:Int){
        val builder = AlertDialog.Builder(this)
        val binding = DialogSanphamBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        val maSP = binding.dialogSpMaSP
        img = binding.dialogSpImg
        val tenSP = binding.dialogSpTenSP
        val gia = binding.dialogSpGia
        val maLoai = intent.getStringExtra("maLoai").toString()

        if(type==1){
            maSP.editText!!.isEnabled = false
            maSP.editText!!.setText(sanPham.maSP)
            tenSP.editText!!.setText(sanPham.tenSP)
            gia.editText!!.setText(sanPham.giaSP.toString())
            if(sanPham.img!=""){
                img.setImageBitmap(TempFunc.StringToBitmap(sanPham.img))
                bitmapSP = TempFunc.StringToBitmap(sanPham.img)
            }
        }

        img.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1).start(this)
        }

        binding.dialogSpSaveBtn.setOnClickListener {
            val sanPham = SanPham(maSP.editText!!.text.toString(),tenSP.editText!!.text.toString(),gia.editText!!.text.toString().toInt(),maLoai,if(bitmapSP==null) "" else  TempFunc.BitMapToString(bitmapSP!!))
            DAO(this).insert(sanPham,"SanPham")
            alertDialog.dismiss()
        }

        binding.dialogSpCancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                val bitmap = MediaStore.Images.Media.getBitmap(this.contentResolver,result.uri)
                bitmapSP = bitmap
                img.setImageBitmap(bitmapSP)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
                Log.d("error",error.toString())
            }
        }
    }
}