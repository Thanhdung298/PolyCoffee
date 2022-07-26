package com.example.polycoffee.fragments

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.example.polycoffee.LoginActivity
import com.example.polycoffee.R
import com.example.polycoffee.adapter.AdapterUser
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.dao.TempFunc
import com.example.polycoffee.databinding.DialogProfileBinding
import com.example.polycoffee.databinding.DialogUserBinding
import com.example.polycoffee.databinding.FragmentProfileBinding
import com.example.polycoffee.databinding.FragmentThongKeBinding
import com.example.polycoffee.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.theartofdev.edmodo.cropper.CropImage

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!
    var user = User()
    lateinit var listUser:ArrayList<User>
    lateinit var adapter: AdapterUser
    lateinit var img: ImageView
    lateinit var imgedit: ImageView

    var bitmapImg: Bitmap?=null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val username = requireActivity().intent.getStringExtra("Username").toString()
        val database = FirebaseDatabase.getInstance().getReference("User").child(username)

        database.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                user = snapshot.getValue(User::class.java)!!
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        img = binding.imgProfile
        imgedit =binding.imageView2
        val role = binding.tvRole
        val hoten = binding.tvProfileHoten
        val diachi = binding.tvProfileDiachi
        val ngaysinh = binding.tvProfileNgaysinh
        val sdt = binding.tvProfileSdt
        val doimk = binding.tvDoiMK
        val logout = binding.tvLogout2
        if (user.role==1){
            role.setText("admin vjp pro pho mai que")
        }else{
            role.setText("Nhân viên quèn")
        }
        hoten.setText(user.hoTen)
        diachi.setText(user.diaChi)
        ngaysinh.setText(user.ngaySinh)
        sdt.setText(user.sdt)
        if(user.anhDaiDien!=""){
            img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
            bitmapImg = TempFunc.StringToBitmap(user.anhDaiDien)
        }
        imgedit.setOnClickListener {
            openDialog(User(),0)
        }
        doimk.setOnClickListener {
//            val intent = Intent(this,DoiPassFragment::class.java)
//            startActivity(intent)
        }
        logout.setOnClickListener {
            startActivity(Intent(requireActivity(),LoginActivity::class.java))
            requireActivity().finish()
        }
        return root
    }
    fun openDialog(user: User,type:Int){
        val builder = AlertDialog.Builder(requireContext())
        val binding = DialogProfileBinding.inflate(layoutInflater)
        builder.setView(binding.root)
        val alertDialog = builder.create()
        alertDialog.show()

        img = binding.dialogProfileImg
        val username = binding.dialogProfileUsername
        val hoten = binding.dialogProfileHoTen
        val ngaySinh = binding.dialogProfileNgaySinh
        val diaChi = binding.dialogProfileDiaChi
        val sdt = binding.dialogProfileSDT
        val saveBtn = binding.dialogProfileBtnSave
        val cancelBtn = binding.dialogProfileBtnCancel


        if(type==1){
            username.editText!!.setText(user.userName)
            username.editText!!.isEnabled = false
            hoten.editText!!.setText(user.hoTen)
            ngaySinh.editText!!.setText(user.ngaySinh)
            diaChi.editText!!.setText(user.diaChi)
            sdt.editText!!.setText(user.sdt)
            if(user.anhDaiDien!=""){
                img.setImageBitmap(TempFunc.StringToBitmap(user.anhDaiDien))
                bitmapImg = TempFunc.StringToBitmap(user.anhDaiDien)
            }
        }

        img.setOnClickListener {
            CropImage.activity().setAspectRatio(1,1).start(requireContext(),this)
        }

        saveBtn.setOnClickListener {
            val userAdd = User(username.editText!!.text.toString(),hoten.editText!!.text.toString(),ngaySinh.editText!!.text.toString(),diaChi.editText!!.text.toString(),sdt.editText!!.text.toString(),if(bitmapImg == null)"" else TempFunc.BitMapToString(bitmapImg!!))
            DAO(requireContext()).insert(userAdd,"User")
            alertDialog.dismiss()
        }
        cancelBtn.setOnClickListener {
            alertDialog.dismiss()
        }


    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}