package com.satydroid.mysys.util

import android.Manifest
import android.app.AlertDialog
import android.app.Service
import android.content.Context
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import com.satydroid.mysys.util.GpsTracker
import com.satydroid.mysys.R
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.os.IBinder
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import java.lang.Exception

class GpsTracker(private val mContext: Context) : Service(), LocationListener {
    var checkGPS = false
    var checkNetwork = false
    var canGetLocation = false
    var loc: Location? = null
    private var latitude = 0.0
    private var longitude = 0.0

    // Declaring a Location Manager
    protected var locationManager: LocationManager? = null

    // Store LocationManager.GPS_PROVIDER or LocationManager.NETWORK_PROVIDER information
    private val provider_info: String? =
        null//     loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

//                }
// TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
// get GPS status
    //            Logger.v("CheckGPS --" + checkGPS);
    // get network provider status
    //            Logger.v("CheckGPS --" + checkNetwork);

//            if (!checkGPS && !checkNetwork) {
//                Toast.makeText(mContext, getString(R.string.no_serivce_provide_is_available), Toast.LENGTH_SHORT).show();
//            } else {

    // if GPS Enabled get lat/long using GPS Services
    /**
     * Try to get my current location by GPS or Network Provider
     */
    private val location: Location?
        private get() {
            try {
                locationManager = mContext.getSystemService(LOCATION_SERVICE) as LocationManager

                // get GPS status
                checkGPS = locationManager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)
                //            Logger.v("CheckGPS --" + checkGPS);
                // get network provider status
                checkNetwork = locationManager!!.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                //            Logger.v("CheckGPS --" + checkNetwork);

//            if (!checkGPS && !checkNetwork) {
//                Toast.makeText(mContext, getString(R.string.no_serivce_provide_is_available), Toast.LENGTH_SHORT).show();
//            } else {
                canGetLocation = true

                // if GPS Enabled get lat/long using GPS Services
                if (checkGPS) {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext, Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return TODO
                    }
                    locationManager!!.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
                        this
                    )
                    if (locationManager != null) {
                        //     loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        loc = lastKnownLocation
                        if (loc != null) {
                            latitude = loc!!.latitude
                            longitude = loc!!.longitude
                        }
                    }

//                }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return loc
        }// Found best last known location: %s", l);

    // TODO: Consider calling
    //    ActivityCompat#requestPermissions
    // here to request the missing permissions, and then overriding
    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
    //                                          int[] grantResults)
    // to handle the case where the user grants the permission. See the documentation
    // for ActivityCompat#requestPermissions for more details.
    private val lastKnownLocation: Location?
        private get() {
            val providers = locationManager!!.getProviders(true)
            var bestLocation: Location? = null
            for (provider in providers) {
                if (ActivityCompat.checkSelfPermission(
                        mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        this,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return TODO
                }
                val l = locationManager!!.getLastKnownLocation(provider) ?: continue
                if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                    // Found best last known location: %s", l);
                    bestLocation = l
                }
            }
            return bestLocation
        }

    fun canGetLocation(): Boolean {
        return canGetLocation
    }

    fun getLongitude(): Double {
        if (loc != null) {
            longitude = loc!!.longitude
        }
        val longi = Location.convert(longitude, Location.FORMAT_DEGREES)
        val longi1 = Location.convert(longitude, Location.FORMAT_MINUTES)
        val longi2 = Location.convert(longitude, Location.FORMAT_SECONDS)
        Log.d("Location----", longi)
        Log.d("Location1----", longi1)
        Log.d("Location2----", longi2)
        Log.d("Location----", longitude.toString() + "")
        return longitude
    }

    fun getLatitude(): Double {
        if (loc != null) {
            latitude = loc!!.latitude
        }
        val lat = Location.convert(latitude, Location.FORMAT_DEGREES)
        val lat1 = Location.convert(latitude, Location.FORMAT_MINUTES)
        val lat2 = Location.convert(latitude, Location.FORMAT_SECONDS)
        Log.d("Location----", lat)
        Log.d("Location1----", lat1)
        Log.d("Location2----", lat2)
        Log.d("Location----", latitude.toString() + "")
        return latitude
    }

    fun showSettingsAlert() {
        val alertDialog = AlertDialog.Builder(
            mContext
        )
        alertDialog.setTitle(mContext.resources.getString(R.string.gps_not_enabled))
        alertDialog.setMessage(mContext.resources.getString(R.string.turn_on_gps))
        alertDialog.setPositiveButton(mContext.resources.getString(R.string.yes)) { dialog, which ->
            val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            mContext.startActivity(intent)
        }
        alertDialog.setNegativeButton(mContext.resources.getString(R.string.no)) { dialog, which -> dialog.cancel() }
        alertDialog.show()
    }

    fun stopListener() {
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    mContext, Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager!!.removeUpdates(this@GpsTracker)
        }
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onLocationChanged(location: Location) {
        loc = location
        location
    }

    override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
    override fun onProviderEnabled(provider: String) {}
    override fun onProviderDisabled(provider: String) {}

    companion object {
        private val TODO: Location? = null

        // The minimum distance to change updates in meters
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 10 meters

        // The minimum time between updates in milliseconds
        private const val MIN_TIME_BW_UPDATES = (1000 * 60 * 1 // 1 minute
                ).toLong()
    }

    init {
        location
    }
}