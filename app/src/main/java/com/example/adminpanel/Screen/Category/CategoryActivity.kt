package com.example.adminpanel.Screen.Category

import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.adminpanel.Adapter.CategoryAdapter
import com.example.adminpanel.ModelClass.ModelClassCategory
import com.example.adminpanel.ModelClass.ModelClassCategoryRead
import com.example.adminpanel.R
import com.example.adminpanel.databinding.ActivityCategoryBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoryActivity : AppCompatActivity() {

    lateinit var categoryBinding: ActivityCategoryBinding
    var list = ArrayList<ModelClassCategoryRead>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        categoryBinding = ActivityCategoryBinding.inflate(layoutInflater)
        setContentView(categoryBinding.root)

        initClick()
        windowColor()
        getCategory()

    }

    private fun initClick() {

        categoryBinding.addCatBtn.setOnClickListener {
            categoryBinding.catData.isVisible = true
        }

        categoryBinding.cancelBtn.setOnClickListener {
            categoryBinding.catData.isVisible = false
        }

        categoryBinding.addBtn.setOnClickListener {
            var m1 = ModelClassCategory(
                categoryBinding.idEdt.text.toString(),
                categoryBinding.catEdt.text.toString()
            )
            setCategory(m1)
            categoryBinding.idEdt.text?.clear()
            categoryBinding.catEdt.text?.clear()
            categoryBinding.idEdt.clearFocus()
            categoryBinding.catEdt.clearFocus()
        }

    }

    private fun setCategory(m1: ModelClassCategory) {

        list.clear()
        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Category").push().setValue(m1)

    }

    private fun getCategory() {

        list.clear()

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Category").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for( x in snapshot.children){
                    var catId = x.child("catId").value.toString()
                    var catName = x.child("catName").value.toString()
                    var key = x.key.toString()

                    var m1 = ModelClassCategoryRead(catId,catName,key)
                    list.add(m1)
                }
                setupRV(list)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun setupRV(list: ArrayList<ModelClassCategoryRead>) {
        var adapter = CategoryAdapter(this, list)
        var layoutManager = LinearLayoutManager(this)
        categoryBinding.rvView.adapter = adapter
        categoryBinding.rvView.layoutManager = layoutManager
    }

    private fun windowColor() {
        val window: Window = this.getWindow()
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.app_color))
    }

}