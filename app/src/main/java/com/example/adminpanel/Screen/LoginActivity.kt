package com.example.adminpanel.Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.adminpanel.R
import com.example.adminpanel.Screen.Data.DataActivity
import com.example.adminpanel.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var intent = Intent(this, DataActivity::class.java)
        startActivity(intent)
        finish()

    }
}