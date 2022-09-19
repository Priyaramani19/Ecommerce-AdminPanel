package com.example.adminpanel.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.adminpanel.Screen.Category.CategoryActivity
import com.example.adminpanel.ModelClass.ModelClassCategoryRead
import com.example.adminpanel.R

class CategoryAdapter(
    val categoryActivity: CategoryActivity,
    val list: ArrayList<ModelClassCategoryRead>
) : RecyclerView.Adapter<CategoryAdapter.ViewData>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewData {
        var view = LayoutInflater.from(categoryActivity).inflate(R.layout.category_item,parent,false)
        return ViewData(view)
    }

    override fun onBindViewHolder(holder: ViewData, position: Int) {
        holder.catId.text = list[position].catId
        holder.catName.text = list[position].catName
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewData(itemView: View) : RecyclerView.ViewHolder(itemView){
        var catId = itemView.findViewById<TextView>(R.id.catId)
        var catName = itemView.findViewById<TextView>(R.id.catName)
    }

}
