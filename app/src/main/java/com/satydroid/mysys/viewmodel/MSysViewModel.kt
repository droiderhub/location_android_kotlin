package com.satydroid.mysys.viewmodel

import android.app.Application
import android.content.Context
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.satydroid.mysys.util.GpsTracker
import com.satydroid.mysys.util.Logger
import java.lang.StringBuilder

class MSysViewModel(application: Application) : AndroidViewModel(application){

    private val context = getApplication<Application>().applicationContext
    lateinit var loc : MutableLiveData<Boolean>
    init {
        loc = MutableLiveData()
    }



    fun getLocationObserve() :LiveData<Boolean>{
        return loc
    }


    fun getLocation(context: Context?): String? {
        var latnlong = "N000000W0000000"
        val gpsTracker = GpsTracker(context!!)
        if (gpsTracker.canGetLocation()) {

            val longitude = gpsTracker.getLongitude()
            val latitude = gpsTracker.getLatitude()
            val builder = StringBuilder()
            if (latitude < 0) {
                builder.append("S")
            } else {
                builder.append("N")
            }

            val latitudeDegrees = Location.convert(
                Math.abs(latitude), Location.FORMAT_SECONDS
            )
            //            String latitudeDegrees = Location.convert(Math.abs(lat), Location.FORMAT_SECONDS);
            Logger.s("latitudeDegrees --$latitudeDegrees")
            val latitudeSplit = latitudeDegrees.split(":".toRegex()).toTypedArray()
            builder.append(
                addZeroWithCordinate(
                    latitudeSplit[0], 2
                )
            )
            builder.append(
                addZeroWithCordinate(
                    latitudeSplit[1], 2
                )
            )
            builder.append(
                addZeroWithCordinate(
                    latitudeSplit[2], 2
                )
            )
            if (longitude < 0) {
                builder.append("W")
            } else {
                builder.append("E")
            }
            val longitudeDegrees = Location.convert(Math.abs(longitude), Location.FORMAT_SECONDS)
            Logger.s("longitudeDegrees --$longitudeDegrees")
            val longitudeSplit = longitudeDegrees.split(":".toRegex()).toTypedArray()
            builder.append(
                addZeroWithCordinate(
                    longitudeSplit[0], 3
                )
            )
            builder.append(
                addZeroWithCordinate(
                    longitudeSplit[1], 2
                )
            )
            builder.append(
                addZeroWithCordinate(
                    longitudeSplit[2], 2
                )
            )
            Logger.s("builder -$builder")
            latnlong = builder.toString()
            loc.value= true
            return builder.toString()
        } else {
            loc.value= false
            gpsTracker.showSettingsAlert()
        }
        return latnlong.replace(".".toRegex(), "0")
    }

    private fun addZeroWithCordinate(`val`: String, i: Int): String? {
        val s = `val`.toFloat().toInt().toString() + ""
        Logger.s("val -$s")
        return if (s.trim { it <= ' ' }.length < i) addZero(i - s.trim { it <= ' ' }.length) + s else if (s.trim { it <= ' ' }.length > i) s.substring(
            0,
            i
        ) else s
    }

    private fun addZero(zero: Int): String {
        var s = ""
        for (i in 0 until zero) s = s + "0"
        return s
    }

}