package com.example.polycoffee

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.polycoffee.dao.DAO
import com.example.polycoffee.databinding.ActivityLoginBinding
import com.example.polycoffee.model.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity(){
    lateinit var binding:ActivityLoginBinding
    private lateinit var dao: DAO
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = DAO(this)
        val list = ArrayList<User>()
        val preferences = getSharedPreferences("USER ACCOUNT", MODE_PRIVATE)
        binding.edLoginUsername.setText(preferences.getString("username",""))
        binding.edLoginPassword.setText(preferences.getString("password",""))
        binding.rdoLoginRMB.isChecked = preferences.getBoolean("check",false)

        binding.btnLogin.setOnClickListener {
            val database = FirebaseDatabase.getInstance().getReference("User")
            database.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (snap in snapshot.children){
                        list.add(snap.getValue(User::class.java)!!)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }


    }
    fun rememberUser(user: String,password:String,check:Boolean){
        val preferences = getSharedPreferences("USER ACCOUNT", MODE_PRIVATE)
        val edit = preferences.edit()
        if (!check){
            edit.clear()
        }else{
            edit.putString("username",user)
            edit.putString("password",password)
            edit.putBoolean("check",check)
        }
        edit.apply()
    }
    fun checkLogin(){
        val username = binding.edLoginUsername.text.toString()
        val password = binding.edLoginPassword.text.toString()
        val checkbox = binding.rdoLoginRMB


        if (username.isEmpty()||password.isEmpty()){
            Toast.makeText(this@LoginActivity,"Tên đăng nhập và mật khẩu không được bỏ trống",
                Toast.LENGTH_SHORT).show()
        } else{
//            if(username.equals()){
//                Toast.makeText(this@LoginActivity,"Login thanh cong", Toast.LENGTH_SHORT).show()
//                rememberUser(username,password,checkbox.isChecked)
//                val intent = Intent(this,MainActivity::class.java)
//                intent.putExtra("user",username)
//                startActivity(intent)
//                finish()
//            } else{
//                Toast.makeText(this@LoginActivity,"Tên đăng nhập và mật khẩu không đúng", Toast.LENGTH_SHORT).show()
//            }
        }
    }
}