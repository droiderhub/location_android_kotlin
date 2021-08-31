package com.satydroid.mysys.util

import android.util.Log

 class Logger {
    //to create static variable
   companion object {
       fun s(log: Int) {
           Log.d("droider","---$log")
       }

       fun s(log:String){
           Log.d("droider","---$log")
       }
   }


}