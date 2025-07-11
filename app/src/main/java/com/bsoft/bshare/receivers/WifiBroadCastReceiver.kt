package com.bsoft.bshare.receivers

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.p2p.WifiP2pManager
import androidx.annotation.RequiresPermission

abstract class WifiBroadCastReceiver(context: Context): BroadcastReceiver() {

    private val connectivityManager by lazy {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val caps = connectivityManager.getNetworkCapabilities(network)
            if (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)) {
                connectionChanged(true)
            }
        }

        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        override fun onLost(network: Network) {
            super.onLost(network)
            val caps = connectivityManager.getNetworkCapabilities(network)
            if (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)) {
                connectionChanged(false)
            }
        }
    }

    private val intentFilter: IntentFilter = IntentFilter().apply {
        addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
        addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
    }

    abstract fun connectionChanged(isConnected: Boolean)
    abstract fun devicesListChanged()
    abstract  fun wifiStateChanged(enabled: Boolean)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES, Manifest.permission.ACCESS_NETWORK_STATE])
    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                wifiStateChanged(enabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
            }
            WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> devicesListChanged()
            WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                connectivityManager.registerNetworkCallback(
                    NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),
                    networkCallback
                )
            }
        }
    }

    @Throws(Exception::class)
    fun register(context: Context){
        context.registerReceiver(this, intentFilter)
    }

    fun unregister(context: Context) {
        try {
            context.unregisterReceiver(this)
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered
        }
    }
}