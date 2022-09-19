package com.example.adminpanel.Screen.Data

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.example.adminpanel.ModelClass.ModelClassCategoryRead
import com.example.adminpanel.ModelClass.ModelClassInsert
import com.example.adminpanel.R
import com.example.adminpanel.databinding.ActivityDataFormBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.ArrayList

class DataFormActivity : AppCompatActivity() {

    var downloadUri: Uri? = null
    lateinit var file: File
    lateinit var category: String
    lateinit var catId: String
    lateinit var uri: Uri

    var catArray = arrayOf<String>()
    var list = ArrayList<ModelClassCategoryRead>()

    lateinit var dataFormBinding: ActivityDataFormBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataFormBinding = ActivityDataFormBinding.inflate(layoutInflater)
        setContentView(dataFormBinding.root)

        initClick()
        windowColor()
        getCategory()
        txtUnderline()

    }

    private fun windowColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.app_color)
    }

    private fun initClick() {

        dataFormBinding.addProduct.setOnClickListener {
            dataFormBinding.progressBar.isVisible = true
            dataFormBinding.alpha.isVisible = true
            uploadImage()
        }

        dataFormBinding.addImage.setOnClickListener {
            var intent = Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
            startActivityForResult(intent, 100)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 100) {
            uri = data?.data!!
            dataFormBinding.img.isVisible = false
            dataFormBinding.dottedLine.isVisible = false
            dataFormBinding.image.setImageURI(uri)
        }
    }

    private fun getCategory() {

        list.clear()

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (x in snapshot.children) {
                    var catId = x.child("catId").value.toString()
                    var catName = x.child("catName").value.toString()
                    var key = x.key.toString()

                    var m1 = ModelClassCategoryRead(catId, catName, key)
                    list.add(m1)

                    catArray += x.child("catName").value.toString()
                }

                setSpinner(catArray)

                dataFormBinding.catSpinner.onItemSelectedListener =
                    object : AdapterView.OnItemSelectedListener {
                        override fun onItemSelected(
                            p0: AdapterView<*>?,
                            p1: View?,
                            p2: Int,
                            p3: Long
                        ) {
                            category = list[p2].catName
                            catId = list[p2].catId
                        }

                        override fun onNothingSelected(p0: AdapterView<*>?) {
                            TODO("Not yet implemented")
                        }

                    }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

    }

    private fun setSpinner(catArray: Array<String>) {
        var adapter = ArrayAdapter(this, android.R.layout.simple_expandable_list_item_1, catArray)
        dataFormBinding.catSpinner.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun insertData(m1: ModelClassInsert) {

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Product").push().setValue(m1)

        dataFormBinding.progressBar.isVisible = false
        dataFormBinding.alpha.isVisible = false

        Toast.makeText(this@DataFormActivity, "Add Product Successfully!!", Toast.LENGTH_SHORT)
            .show()

        var intent = Intent(this, DataActivity::class.java)
        startActivity(intent)
    }

    private fun uploadImage() {

        file = File(uri.toString())

        var storage = Firebase.storage
        var storageReference = storage.reference.child("${file.name}")

        var uploadTask = storageReference.putFile(uri)

        uploadTask.addOnSuccessListener { response ->

            storageReference.downloadUrl.addOnSuccessListener { uri ->

                if (uri != null) {
                    downloadUri = uri
                    var m1 = ModelClassInsert(
                        dataFormBinding.nameEdt.text.toString(),
                        dataFormBinding.descEdt.text.toString(),
                        category,
                        catId,
                        dataFormBinding.priceEdt.text.toString(),
                        dataFormBinding.offerEdt.text.toString(),
                        downloadUri.toString()
                    )
                    insertData(m1)
                }

            }.addOnFailureListener { error ->
                Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener { error ->
            Toast.makeText(this, "${error.message}", Toast.LENGTH_SHORT).show()
        }

    }

    private fun txtUnderline() {
        val content = SpannableString("+Add Image")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        dataFormBinding.addImage.setText(content)
    }

}