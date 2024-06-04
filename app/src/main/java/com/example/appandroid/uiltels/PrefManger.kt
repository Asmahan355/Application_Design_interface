package com.example.appandroid.uiltels

import android.content.Context
import android.content.SharedPreferences

class PrefManger(var contex:Context){
    lateinit var isFirstTimeLaunch: Any
    lateinit var pref:SharedPreferences
    lateinit var editor:SharedPreferences.Editor
    //
    var PRIVATE_MODE=0
    var isFirstTimLaunch :Boolean
        get()=pref.getBoolean(IS_FIRST_TIME_LAUNCH,true)
        set(isFirstTime) {
            editor.putBoolean(IS_FIRST_TIME_LAUNCH,isFirstTime)
            editor.commit()
        }
    companion object{
        private const val PREF_NAME="androidhive-welcome"
        private const val IS_FIRST_TIME_LAUNCH="IsFirstTimeLaunch"

    }
    init{
        pref= contex.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor=pref.edit()
    }
}

