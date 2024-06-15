package com.example.appandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.appandroid.R
import com.example.appandroid.model.CategoryData

class CategoryAdapter (val c:Context,val categoryList:ArrayList<CategoryData> ):
RecyclerView.Adapter<CategoryAdapter.CategoryHolder>()
{
    inner class CategoryHolder(val v: View):RecyclerView.ViewHolder(v){
     val categoryImg=v.findViewById<ImageView>(R.id.catIm)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryHolder {
       val infalter=LayoutInflater.from(parent.context)
        val v=infalter.inflate(R.layout.item_category,parent,false)
        return CategoryHolder(v)
    }

    override fun getItemCount(): Int=categoryList.size

    override fun onBindViewHolder(holder: CategoryHolder, position: Int) {
      val listCat=categoryList[position]
       holder.categoryImg.setImageResource(listCat.imag)
    }

}
