package com.bsoft.bshare

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.net.wifi.WifiManager
import android.net.wifi.p2p.WifiP2pManager
import android.util.Log

class WiFiDirectFileTransferApp : Application() {
    val wifiP2pManager: WifiP2pManager? by lazy {
        // Device capability definition check
        if (!this.packageManager.hasSystemFeature(PackageManager.FEATURE_WIFI_DIRECT)) {
            Log.e(TAG, "Wi-Fi Direct is not supported by this device.")
            return@lazy null
        }

        // Hardware capability check
        val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager?
        if (wifiManager == null) {
            Log.e(TAG, "Cannot get Wi-Fi system service.")
            return@lazy null
        }

        if (!wifiManager.isP2pSupported) {
            Log.e(TAG, "Wi-Fi Direct is not supported by the hardware or Wi-Fi is off.")
            return@lazy null
        }
        getSystemService(Context.WIFI_P2P_SERVICE) as WifiP2pManager?
    }

    val channel: WifiP2pManager.Channel? by lazy {
        wifiP2pManager.let {
            if(it == null){
                return@let null
            }
            return@let wifiP2pManager!!.initialize(this, mainLooper, null)
        }
    }

    companion object{
        private const val TAG: String = "WifiDirectFileTransfer"
    }
}