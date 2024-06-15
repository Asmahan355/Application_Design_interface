package com.example.appandroid.view

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Recycler
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.appandroid.R
import com.example.appandroid.R.id
import com.example.appandroid.adapter.CategoryAdapter
import com.example.appandroid.adapter.SliderAdapter
import com.example.appandroid.model.CategoryData
import com.example.appandroid.model.SliderItem
import com.example.appandroid.uiltels.categoryItemList
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot


class HomeFragment : Fragment() {

    private lateinit var catList: ArrayList<CategoryData>
    private lateinit var catAdapter:CategoryAdapter
    private lateinit var catRec:RecyclerView

    private lateinit var viewPagerImageSlider: ViewPager2
    private lateinit var sliderItemList:ArrayList<SliderItem>
    private lateinit var sliderAdapter:SliderAdapter
    private lateinit var sliderHandle:Handler
    private lateinit var sliderRun:Runnable

   var firebaseFireStore:FirebaseFirestore?=null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View? {
        // Inflate the layout for this fragment
              return inflater.inflate(R.layout.fragment_home, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initss(view)
    }

    private fun initss(v:View)
    {
        viewPagerImageSlider=v.findViewById(R.id.viewPagerImageSlider)

        catgoryData(v)
        sliderView()
    }

    fun catgoryData(v:View)
    {
        catRec=v.findViewById<RecyclerView>( R.id.categoryRecycler)
        catList= ArrayList()
        categoryItemList(catList)

        catAdapter= CategoryAdapter(
             v.context,catList
        )

        catRec.layoutManager=LinearLayoutManager(
            v.context,LinearLayoutManager.HORIZONTAL,false
        )
        catRec.adapter=catAdapter
    }

    private fun sliderView() {
        sliderItemList=ArrayList()
        firebaseFireStore=FirebaseFirestore.getInstance()
        firebaseFireStore!!.collection("BANNER").get()
            .addOnCompleteListener(object :OnCompleteListener<QuerySnapshot>{
                @SuppressLint("SuspiciousIndentation")
                override fun onComplete(task: Task<QuerySnapshot>) {
                      if(task.isSuccessful)
                       {
                         for(documentSnapShot in task.result!! )
                         {
                                val sliderItem=SliderItem(
                                    documentSnapShot.get("sliderimg").toString())
                                   sliderItemList.add(sliderItem)

                         }
                          }
                  }


            })


        sliderAdapter= SliderAdapter(requireActivity(),viewPagerImageSlider,sliderItemList)

        viewPagerImageSlider.adapter=sliderAdapter
        viewPagerImageSlider.clipToPadding=false
        viewPagerImageSlider.clipChildren=false
        viewPagerImageSlider.offscreenPageLimit=3
        viewPagerImageSlider.getChildAt(0)
            .overScrollMode=RecyclerView.OVER_SCROLL_NEVER
        val composPageTarn=CompositePageTransformer()
        composPageTarn.addTransformer(MarginPageTransformer(40))
        composPageTarn.addTransformer(object :ViewPager2.PageTransformer{
            override fun transformPage(page: View, position: Float) {
            val r:Float=1- Math.abs(position)
                page.scaleY=0.85f +r * 0.15f

            }

        })
        viewPagerImageSlider.setPageTransformer(composPageTarn)

        sliderHandle= Handler()
        sliderRun=object :Runnable{
            override fun run() {
                viewPagerImageSlider
                    .setCurrentItem(viewPagerImageSlider
                        .currentItem +1)


            }

        }

        viewPagerImageSlider
            .registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback(){
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    sliderHandle.removeCallbacks(sliderRun)
                    sliderHandle.postDelayed(sliderRun,3000)
                }

        })


    }

}
