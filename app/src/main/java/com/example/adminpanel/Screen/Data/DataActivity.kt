package com.example.adminpanel.Screen.Data

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpanel.Screen.Category.CategoryActivity
import com.example.adminpanel.Adapter.DataAdapter
import com.example.adminpanel.ModelClass.ModelClassRead
import com.example.adminpanel.R
import com.example.adminpanel.databinding.ActivityDataBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DataActivity : AppCompatActivity() {

    lateinit var dataBinding: ActivityDataBinding
    var list = ArrayList<ModelClassRead>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = ActivityDataBinding.inflate(layoutInflater)
        setContentView(dataBinding.root)

        readData()
        initClick()
        windowColor()

    }

    private fun windowColor() {
        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
    }

    private fun initClick() {
        dataBinding.addBtn.setOnClickListener {
            var intent = Intent(this, DataFormActivity::class.java)
            startActivity(intent)
        }

        dataBinding.categoryBtn.setOnClickListener {
            var intent = Intent(this, CategoryActivity::class.java)
            startActivity(intent)
        }

    }

    private fun readData() {
        list.clear()
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Product").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (x in snapshot.children) {

                    var productName = x.child("name").value.toString()
                    var productDesc = x.child("desc").value.toString()
                    var productPrice = x.child("price").value.toString()
                    var productUri = x.child("uri").value.toString()
                    var productOffer = x.child("offer").value.toString()
                    var productCat = x.child("category").value.toString()
                    var productCatId = x.child("catId").value.toString()
                    var key = x.key.toString()

                    var m1 = ModelClassRead(
                        productName,
                        productDesc,
                        productPrice,
                        productOffer,
                        productUri,
                        productCat,
                        productCatId,
                        key
                    )
                    list.add(m1)

                }
                setupRV(list)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@DataActivity, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun setupRV(list: ArrayList<ModelClassRead>) {
        var adapter = DataAdapter(this, list)
        var layoutManager = LinearLayoutManager(this)
        dataBinding.rvView.adapter = adapter
        dataBinding.rvView.layoutManager = layoutManager
    }
}