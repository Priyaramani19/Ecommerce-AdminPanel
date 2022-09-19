package com.example.adminpanel.Screen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import com.example.adminpanel.R
import com.example.adminpanel.databinding.ActivitySplashScreenBinding
import com.google.firebase.auth.FirebaseAuth

class SplashScreenActivity : AppCompatActivity() {

    lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        windowColor()

        Handler().postDelayed({

            var user = FirebaseAuth.getInstance().currentUser

            if (user != null) {
                var intent = Intent(this, SplashScreenActivity::class.java)
                startActivity(intent)
            } else {
                var intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
            }

            finish()

        }, 3000)

    }

    private fun windowColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
    }

}