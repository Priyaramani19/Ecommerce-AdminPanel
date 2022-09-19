package com.example.adminpanel.ModelClass

import android.net.Uri

data class ModelClassInsert(
    val name: String,
    val desc: String,
    val category: String,
    val catId : String,
    val price: String,
    val offer: String,
    val uri : String
) {

}
