package com.example.appandroid.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.appandroid.R
import com.example.appandroid.model.SliderItem
import com.bumptech.glide.Glide


class SliderAdapter(
    val context: Context,val viewPage:ViewPager2,val imgList:ArrayList<SliderItem>

):RecyclerView.Adapter<SliderAdapter.SliderViewHolder>()
{
   inner class SliderViewHolder(val v:View):RecyclerView.ViewHolder(v) {
        val imgView=v.findViewById<ImageView>(R.id.imageSlider)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val infalter= LayoutInflater.from(parent.context)
        val v=infalter.inflate(R.layout.slider_item,parent,false)
        return SliderViewHolder(v)
    }

    override fun getItemCount(): Int =imgList.size

    val run=object :Runnable{
        override fun run() {
         imgList.addAll(imgList)
            notifyDataSetChanged()
        }

    }
    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        val listImg=imgList[position]
        Glide.with(context).load(listImg.sliderimg)
            .placeholder(R.drawable.avatar)
            .into(holder.imgView)
        if(position==imgList.size -2){
          viewPage.post(run)
        }



    }
}