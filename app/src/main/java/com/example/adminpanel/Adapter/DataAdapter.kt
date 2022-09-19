package com.example.adminpanel.Adapter

import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.adminpanel.Screen.Data.DataActivity
import com.example.adminpanel.ModelClass.ModelClassRead
import com.example.adminpanel.R
import com.example.adminpanel.Screen.Update.UpdateActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.database.FirebaseDatabase

class DataAdapter(val dataActivity: DataActivity, val list: ArrayList<ModelClassRead>) :
    RecyclerView.Adapter<DataAdapter.ViewData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
        var view = LayoutInflater.from(dataActivity).inflate(R.layout.data_item, parent, false)
        return ViewData(view)
    }

    override fun onBindViewHolder(holder: ViewData, position: Int) {
        holder.productName.text = list[position].name
        holder.productDesc.text = list[position].desc
        holder.productPrice.text = list[position].price

        Glide.with(dataActivity).load(list[position].uri).into(holder.productImage)

        holder.options.setOnClickListener {
            openBottomShit(position)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var productName = itemView.findViewById<TextView>(R.id.productName)
        var productDesc = itemView.findViewById<TextView>(R.id.productDesc)
        var productPrice = itemView.findViewById<TextView>(R.id.productPrice)
        var productImage = itemView.findViewById<ImageView>(R.id.productImage)
        var options = itemView.findViewById<ImageView>(R.id.options)
    }


    private fun openBottomShit(position: Int) {
        var dialog = BottomSheetDialog(dataActivity)
        var view = LayoutInflater.from(dataActivity).inflate(R.layout.bottom_sheet_dialog, null)
        dialog.setContentView(view)
        dialog.show()

        var update = view.findViewById<RelativeLayout>(R.id.update)
        var delete = view.findViewById<RelativeLayout>(R.id.delete)

        update.setOnClickListener {
            var intent = Intent(dataActivity, UpdateActivity::class.java)
            intent.putExtra("name", list[position].name)
            intent.putExtra("desc", list[position].desc)
            intent.putExtra("price", list[position].price)
            intent.putExtra("offer", list[position].offer)
            intent.putExtra("uri", list[position].uri)
            intent.putExtra("cat", list[position].category)
            intent.putExtra("catId", list[position].catId)
            intent.putExtra("key", list[position].key)
            dataActivity.startActivity(intent)
            dialog.dismiss()
        }

        delete.setOnClickListener {

            openDialog(position)
            dialog.dismiss()

        }

    }

    private fun openDialog(position: Int) {
        var dialog = Dialog(dataActivity)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_dialog)
        dialog.show()

        var closeBtn = dialog.findViewById<TextView>(R.id.closeBtn)
        var yesBtn = dialog.findViewById<TextView>(R.id.yesBtn)

        closeBtn.setOnClickListener {
            dialog.dismiss()
        }

        yesBtn.setOnClickListener {
            var firebaseDatabase = FirebaseDatabase.getInstance()
            var firebaseReference = firebaseDatabase.reference

            firebaseReference.child("Product").child(list[position].key).removeValue()
            list.clear()
            dialog.dismiss()
        }

    }

}