package com.example.appandroid.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.ViewPager
import com.example.appandroid.R
import com.example.appandroid.adapter.MyViewPagerAdapter
import com.example.appandroid.uiltels.PrefManger
import com.google.firebase.FirebaseApp

class SliderActivity : AppCompatActivity() {
    private var viewPager:ViewPager?=null //id de view pager de slider_activity.xml
    private var myViewPagerAdapter:MyViewPagerAdapter?=null
    private var dotsLayout:LinearLayout?=null
    private lateinit var dots : Array<TextView?>
    private lateinit var layouts:IntArray
    private var bntSkip: Button? = null
    private var bntNext:Button? = null
    private var prefManger:PrefManger? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_slider)
        FirebaseApp.initializeApp(this);
        // checking for first time launch - before calling setContebtView()
        prefManger= PrefManger(this)
        if(!prefManger!!.isFirstTimeLaunch)
        {
            launchHomeScreen()
            finish()
        }
       //Making notification bar transparent
       if(Build.VERSION.SDK_INT>=21)
       {
           window.decorView.systemUiVisibility
           View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_FULLSCREEN

       }
        setContentView(R.layout.activity_slider)
        viewPager=findViewById<ViewPager>(R.id.view_pager)
        dotsLayout=findViewById(R.id.layout_dots)
        bntSkip=findViewById(R.id.btn_skip)
        bntNext=findViewById(R.id.btn_next)
        //layout of all welcome slider
        //add few more layouts if you want
        layouts= intArrayOf(
            R.layout.welcome_slider1,
            R.layout.welcome_slider2,
            R.layout.welcome_slider3,
            R.layout.welcome_slider4,


            )
        //adding bottom dots
        addBottomDots(0)
        //making notification bar transparent
        changeStatusBarColor()
        myViewPagerAdapter= MyViewPagerAdapter(this,layouts)
        viewPager!!.adapter=myViewPagerAdapter
        viewPager!!.addOnPageChangeListener(viewPagerChangeListener)
        bntSkip!!.setOnClickListener {                  //'!!' signifie
            launchHomeScreen()                                   // que le développeur est sûr que viewPager
                                                               // n'est pas nul à ce moment-là et que l'utilisation
                                                                 // de !! lève une exception si viewPager est en fait nul
        }
        bntNext!!.setOnClickListener {
            //checking for last page
            //if last page home screen will be launched
            val current=getItem(+1)
            if(current<layouts.size)
            {
                //more to next screen
                viewPager!!.currentItem=current
            }else{
                launchHomeScreen()
            }
        }

    }

    private fun getItem(i: Int): Int {
        return viewPager!!.currentItem+i
    }

    var viewPagerChangeListener:ViewPager.OnPageChangeListener=
        object : ViewPager.OnPageChangeListener
        {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                addBottomDots(position)
                //changing the text button text 'NEXT'/'GOT IT'
              if(position==layouts.size-1)
              {
                  //lastpage make next to got it
                  bntNext!!.text=getString(R.string.start)
                  bntSkip!!.visibility=View.GONE
              }else{
                  bntNext!!.text=getString(R.string.next)
                  bntSkip!!.visibility=View.VISIBLE
              }

            }

            override fun onPageSelected(position: Int) {}

            override fun onPageScrollStateChanged(state: Int) {}

        }
    private fun changeStatusBarColor() {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            val window=window
            window.statusBarColor=Color.TRANSPARENT

        }

    }

    private fun addBottomDots(currentPage: Int) {
        dots= arrayOfNulls(layouts.size)
        val colorActive=resources.getIntArray(R.array.array_dot_active)
        val colorInActive=resources.getIntArray(R.array.array_dot_inactive)
        dotsLayout!!.removeAllViews()
        for(i in dots.indices) {
            dots[i]= TextView(this)
            dots[i]!!.text=Html.fromHtml("&#8226")
            dots[i]!!.textSize=35f
            dots[i]!!.setTextColor(colorInActive[currentPage])
            dotsLayout!!.addView(dots[i])
        }
        if(dots.size>0) {
            dots[currentPage]!!.setTextColor(colorActive[currentPage])

        }

    }


    private fun launchHomeScreen() {//la page qui sera affichée
        prefManger!!.isFirstTimeLaunch=false
        startActivity(Intent(this,VerificationActivity::class.java))
        finish()
     }
    }