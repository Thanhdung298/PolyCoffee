package com.example.polycoffee

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
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
import com.theartofdev.edmodo.cropper.CropImageView

class SubMenuActivity : AppCompatActivity() {
    lateinit var binding:ActivitySubMenuBinding
    var bitmapSP:Bitmap? = null
    var listSP = ArrayList<SanPham>()
    lateinit var img:ImageView
    lateinit var recyclerView: RecyclerView
    lateinit var adapterSP: AdapterSP
   // val type = intent.getIntExtra("types",0)
   var type = 0
    var maBan = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySubMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)
        type = intent.getIntExtra("types",0).toString().toInt()
        maBan = intent.getStringExtra("maBan").toString()

        if(type==1){
            binding.subMenuFab.isVisible = false
        }
        binding.subMenuFab.setOnClickListener {
            val maLoai = intent.getStringExtra("maLoai").toString()
            openDialogSP(SanPham(),0,this@SubMenuActivity,maLoai)
        }
        updateRecyclerView()
        getListLSP(listSP,adapterSP)



    }

    fun getListLSP(list:ArrayList<SanPham>,adapterSP: AdapterSP){
        list.clear()
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
                list.sortWith(compareBy { it.maSP })
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
        adapterSP = AdapterSP(this,listSP,type,maBan,this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapterSP
    }

    fun openDialogSP(sanPham: SanPham, type:Int,context: Context,maLoai:String = ""){
        val builder = AlertDialog.Builder(context)
        val binding = DialogSanphamBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.show()

        val maSP = binding.dialogSpMaSP
        img = binding.dialogSpImg
        val tenSP = binding.dialogSpTenSP
        val gia = binding.dialogSpGia

        maSP.editText!!.isEnabled = false

        if(type==1){
            maSP.editText!!.setText(sanPham.maSP.toString())
            tenSP.editText!!.setText(sanPham.tenSP)
            gia.editText!!.setText(sanPham.giaSP.toString())
            if(sanPham.img!=""){
                img.setImageBitmap(TempFunc.StringToBitmap(sanPham.img))
                bitmapSP = TempFunc.StringToBitmap(sanPham.img)
            }
        }

        img.setOnClickListener {
            CropImage.activity().setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(1,1).start(
                context as Activity
            )
        }
        if(type==0){
            val database = FirebaseDatabase.getInstance().getReference("SanPham")
            database.get().addOnSuccessListener {
                val sp = it.children.lastOrNull()?.getValue(SanPham::class.java)
                if(sp!=null){
                    maSP.editText!!.setText("${sp.maSP+1}")
                } else{
                    maSP.editText!!.setText("${0}")
                }
            }
        }


        binding.dialogSpSaveBtn.setOnClickListener {
            TempFunc.checkField(tenSP,gia)
            if(!"^[0-9]+$".toRegex().matches(gia.editText!!.text.toString())){
                gia.error = "Giá tiền phải là số"
            } else gia.error = null
            if(TempFunc.noError(tenSP,gia)){
                val sanPhamSub = SanPham(maSP.editText!!.text.toString().toInt(),tenSP.editText!!.text.toString(),gia.editText!!.text.toString().toInt(),if(type==0) maLoai else sanPham.maLoai,if(bitmapSP==null) "" else  TempFunc.BitMapToString(bitmapSP!!))
                FirebaseDatabase.getInstance().getReference("SanPham").child(maSP.editText!!.text.toString()).setValue(sanPhamSub)
                    .addOnFailureListener { Toast.makeText(context,"That bai",Toast.LENGTH_SHORT).show() }
                    .addOnSuccessListener { Toast.makeText(context,"Thanh cong",Toast.LENGTH_SHORT).show()}
                alertDialog.dismiss()
            }
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