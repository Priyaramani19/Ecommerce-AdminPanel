package com.example.adminpanel.Screen.Update

import android.R
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.adminpanel.ModelClass.ModelClassCategoryRead
import com.example.adminpanel.ModelClass.ModelClassInsert
import com.example.adminpanel.Screen.Data.DataActivity
import com.example.adminpanel.databinding.ActivityUpdateBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.util.ArrayList

class UpdateActivity : AppCompatActivity() {

    var catId : Int? = null
    lateinit var uriS: String
    lateinit var file: File
    lateinit var name: String
    lateinit var price: String
    lateinit var desc: String
    lateinit var offer: String
    lateinit var uri: Uri
    lateinit var cat: String
    lateinit var categoryId: String
    lateinit var key: String
    lateinit var category : String

    lateinit var updateBinding: ActivityUpdateBinding

    var downloadUri: Uri? = null
    var list = ArrayList<ModelClassCategoryRead>()
    var catArray = arrayOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateBinding = ActivityUpdateBinding.inflate(layoutInflater)
        setContentView(updateBinding.root)

        name = intent.getStringExtra("name").toString()
        price = intent.getStringExtra("price").toString()
        desc = intent.getStringExtra("desc").toString()
        offer = intent.getStringExtra("offer").toString()
        uriS = intent.getStringExtra("uri").toString()
        cat = intent.getStringExtra("cat").toString()
        catId = intent.getStringExtra("catId")?.toInt()
        key = intent.getStringExtra("key").toString()
        uri = Uri.parse(uriS)


        updateData()
        initClick()
        getCategory()
        windowColor()

    }

    private fun initClick() {

        updateBinding.updateProduct.setOnClickListener {
            updateBinding.progressBar.isVisible = true
            updateBinding.alpha.isVisible = true
            uploadImage()
        }

        updateBinding.updateImage.setOnClickListener {
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
            updateBinding.image.setImageURI(uri)

        }
    }

    private fun windowColor() {
        val window: Window = this.window
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, com.example.adminpanel.R.color.app_color)
    }

    private fun updateData() {
        updateBinding.nameEdt.setText(name)
        updateBinding.descEdt.setText(desc)
        updateBinding.priceEdt.setText(price)
        updateBinding.offerEdt.setText(offer)

        Glide.with(this).load(uri).into(updateBinding.image)
    }


    private fun getCategory() {

        list.clear()

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Category").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for( x in snapshot.children){
                    var catId = x.child("catId").value.toString()
                    var catName = x.child("catName").value.toString()
                    var key = x.key.toString()

                    var m1 = ModelClassCategoryRead(catId,catName,key)
                    list.add(m1)

                    catArray += x.child("catName").value.toString()
                }

                setSpinner(catArray)
                updateBinding.catSpinner.setSelection(catId!!-1)

                updateBinding.catSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        category = list[p2].catName
                        categoryId = list[p2].catId
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
        var adapter = ArrayAdapter(this, R.layout.simple_expandable_list_item_1, catArray)
        updateBinding.catSpinner.adapter = adapter
        adapter.notifyDataSetChanged()
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
                        updateBinding.nameEdt.text.toString(),
                        updateBinding.descEdt.text.toString(),
                        category,
                        categoryId,
                        updateBinding.priceEdt.text.toString(),
                        updateBinding.offerEdt.text.toString(),
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

    private fun insertData(m1: ModelClassInsert) {

        var firebaseDatabase = FirebaseDatabase.getInstance()
        var firebaseReference = firebaseDatabase.reference

        firebaseReference.child("Product").child(key).setValue(m1)

        updateBinding.progressBar.isVisible = false
        updateBinding.alpha.isVisible = false

        Toast.makeText(this@UpdateActivity, "Update Product Successfully!!", Toast.LENGTH_SHORT)
            .show()

        var intent = Intent(this, DataActivity::class.java)
        startActivity(intent)
    }


}