package com.example.polycoffee.fragments

import android.R.attr
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.polycoffee.adapter.AdapterMenu
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogLoaispBinding
import com.example.polycoffee.databinding.DialogSanphamBinding
import com.example.polycoffee.databinding.FragmentMenuBinding
import com.example.polycoffee.model.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage
import kotlin.math.log


class MenuFragment : Fragment() {

    lateinit var adapter:AdapterMenu
    lateinit var recyclerView: RecyclerView
    lateinit var listLoaiSP:ArrayList<LoaiSanPham>
    var bitmapLoaiSP:Bitmap? = null
    lateinit var img:ImageView

    private var _binding: FragmentMenuBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {

        _binding = FragmentMenuBinding.inflate(inflater, container, false)
        binding.menuFab.setOnClickListener {
            openDialogLSP(LoaiSanPham(),0)
        }
        updateRecyclerView()
        getListLSP()
        return binding.root
    }

    fun updateRecyclerView(){
        listLoaiSP = ArrayList<LoaiSanPham>()
        recyclerView = binding.menuRecyclerView
        adapter = AdapterMenu(requireContext(),listLoaiSP,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    fun getListLSP(){
        val database = FirebaseDatabase.getInstance().getReference("LoaiSP")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listLoaiSP.clear()
                for (datasnap in snapshot.children){
                    val loaiSanPham = datasnap.getValue(LoaiSanPham::class.java)
                    if (loaiSanPham != null) {
                        listLoaiSP.add(loaiSanPham)
                    }
                }
                listLoaiSP.sortWith(compareBy { it.maLoai })
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"Failed",Toast.LENGTH_SHORT).show()
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    fun openDialogSP(sanPham: SanPham, type:Int){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogSanphamBinding.inflate(layoutInflater)
        builder.setView(binding.root)

        val alertDialog = builder.create()
        alertDialog.show()
    }
    fun openDialogLSP(loaiSanPham: LoaiSanPham, type:Int){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogLoaispBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()
        val maLoai = binding.dialogLoaiSPMaLoai
        val tenLoai = binding.dialogLoaiSPTenLoai
        val saveBtn = binding.dialogLoaiSPSaveBtn
        val cancelBtn = binding.btnLSPCancel
        img = binding.dialogLoaiSPImg

        if(type==1){
            maLoai.editText!!.isEnabled = false
            maLoai.editText!!.setText(loaiSanPham.maLoai)
            tenLoai.editText!!.setText(loaiSanPham.tenLoai)
            img.setImageBitmap(TempFunc.StringToBitmap(loaiSanPham.img))
            bitmapLoaiSP = TempFunc.StringToBitmap(loaiSanPham.img)
        }

        img.setOnClickListener {
            CropImage.activity().setAspectRatio(27,6).start(requireContext(),this)
        }

        saveBtn.setOnClickListener {
            val loaiSP = LoaiSanPham(maLoai.editText!!.text.toString(),tenLoai.editText!!.text.toString(),if(bitmapLoaiSP == null)"" else TempFunc.BitMapToString(bitmapLoaiSP!!))
            DAO(requireContext()).insert(loaiSP,"LoaiSP")
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener{
            alertDialog.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == RESULT_OK){
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,result.uri)
                bitmapLoaiSP = bitmap
                img.setImageBitmap(bitmapLoaiSP)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
                Log.d("error",error.toString())
            }
        }
    }
}