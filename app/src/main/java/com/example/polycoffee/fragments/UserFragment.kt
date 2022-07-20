package com.example.polycoffee.fragments

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
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
import com.example.polycoffee.adapter.AdapterUser
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogUserBinding
import com.example.polycoffee.databinding.FragmentUserBinding
import com.example.polycoffee.model.LoaiSanPham
import com.example.polycoffee.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage

class UserFragment : Fragment() {

    private var _binding: FragmentUserBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    lateinit var adapter:AdapterUser
    lateinit var recyclerView: RecyclerView
    lateinit var listUser:ArrayList<User>
    lateinit var bitmapImg:Bitmap
    lateinit var img:ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentUserBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.fabUser.setOnClickListener {
            openDialog(User(),0)
        }
        updateRecyclerView()
        getListLSP()
        return root
    }

    fun updateRecyclerView(){
        listUser = ArrayList()
        recyclerView = binding.reyclerViewUser
        adapter = AdapterUser(requireContext(),listUser,this)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }
    fun getListLSP(){
        val database = FirebaseDatabase.getInstance().getReference("User")
        database.addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                listUser.clear()
                for (datasnap in snapshot.children){
                    val user = datasnap.getValue(User::class.java)
                    if (user != null) {
                        listUser.add(user)
                    }
                }
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
    fun openDialog(user: User,type:Int){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogUserBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()

        img = binding.dialogUserImg
        val username = binding.dialogUserUsername
        val password = binding.dialogUserPassword
        val hoten = binding.dialogUserHoTen
        val ngaySinh = binding.dialogUserNgaySinh
        val diaChi = binding.dialogUserDiaChi
        val sdt = binding.dialogUserSDT
        val saveBtn = binding.dialogUserBtnSave
        val cancelBtn = binding.dialogUserBtnCancel

        if(type==1){
            username.editText!!.setText(user.userName)
            username.editText!!.isEnabled = false
            password.editText!!.setText(user.passWord)
            hoten.editText!!.setText(user.hoTen)
            ngaySinh.editText!!.setText(user.ngaySinh)
            diaChi.editText!!.setText(user.diaChi)
            sdt.editText!!.setText(user.sDT)
            img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
        }

        img.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1).start(requireContext(),this)
        }

        saveBtn.setOnClickListener {
            val user = User(username.editText!!.text.toString(),password.editText!!.text.toString(),hoten.editText!!.text.toString(),ngaySinh.editText!!.text.toString(),diaChi.editText!!.text.toString(),sdt.editText!!.text.toString(),TempFunc.BitMapToString(bitmapImg))
            DAO(requireContext()).insert(user,"User")
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            val result = CropImage.getActivityResult(data)
            if(resultCode == Activity.RESULT_OK){
                val bitmap = MediaStore.Images.Media.getBitmap(requireContext().contentResolver,result.uri)
                bitmapImg = bitmap
                img.setImageBitmap(bitmapImg)

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                val error = result.error
                Log.d("error",error.toString())
            }
        }
    }
}