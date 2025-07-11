package com.bsoft.bshare.viewModels

import android.Manifest
import android.app.Application
import android.content.Context
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import com.bsoft.bshare.WiFiDirectFileTransferApp
import com.bsoft.bshare.receivers.WifiBroadCastReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ConnectionStatus {
    CONNECTING,
    DISCONNECTED,
    CLIENT,
    HOST
}

data class WifiState(
    val contentedDevice: WifiP2pDevice? = null,
    val devices: List<WifiP2pDevice> = listOf(),
    val isError: Boolean = false,
    val enabled: Boolean = false,
    val connectionStatus: ConnectionStatus = ConnectionStatus.DISCONNECTED,
    val fetchingStarted: Boolean = false,
    val message: String = "")

class WifiDirectViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application.applicationContext as WiFiDirectFileTransferApp
    private val wifiP2pManager = app.wifiP2pManager
    private val channel = app.channel

    private val mutableState = MutableStateFlow(WifiState())
    val state: StateFlow<WifiState> = mutableState.asStateFlow()

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        mutableState.update { it.copy(devices = peerList.deviceList.toList()) }
    }

    private val wifiBroadcastReceiver = object: WifiBroadCastReceiver(application){
        override fun connectionChanged(isConnected: Boolean) {
            if(isConnected){
                wifiP2pManager!!.requestConnectionInfo(channel, connectionListener)
            }
            mutableState.update { it.copy(connectionStatus = ConnectionStatus.DISCONNECTED) }
        }

        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
        override fun devicesListChanged() {
            wifiP2pManager!!.requestPeers(channel, peerListListener)
        }

        override fun wifiStateChanged(enabled: Boolean) {
            mutableState.update { it.copy(enabled = enabled) }
        }
    }

    private val connectionListener = WifiP2pManager.ConnectionInfoListener { info ->
        // String from WifiP2pInfo struct
        val groupOwnerAddress: String = info.groupOwnerAddress.hostAddress

        // After the group negotiation, we can determine the group owner
        // (server).
        if (info.groupFormed && info.isGroupOwner) {
            // Do whatever tasks are specific to the group owner.
            // One common case is creating a group owner thread and accepting
            // incoming connections.
            mutableState.update { it.copy(connectionStatus = ConnectionStatus.HOST) }
        } else if (info.groupFormed) {
            // The other device acts as the peer (client). In this case,
            // you'll want to create a peer thread that connects
            // to the group owner.
            mutableState.update { it.copy(connectionStatus = ConnectionStatus.CLIENT) }
        }
    }

    @Throws(Exception::class)
    fun registerReceiver(context: Context){
        if(channel != null || wifiP2pManager != null){
            wifiBroadcastReceiver.register(context)
        }else{
            throw Exception("${TAG}: wifi is either switched off or not wifi direct is not supported")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
    fun connect(device: WifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }
        mutableState.update { it.copy(message = "connecting to ${device.deviceName}", connectionStatus = ConnectionStatus.CONNECTING, contentedDevice = device) }
        wifiP2pManager!!.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                //mutableState.update { it.copy(contentedDevice = device) }
            }
            override fun onFailure(reason: Int) {
                mutableState.update { it.copy(message = "failed connecting to ${device.deviceName}", connectionStatus = ConnectionStatus.DISCONNECTED) }
            }
        })
    }

    fun unregisterReceiver(context: Context) {
        wifiBroadcastReceiver.unregister(context)
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
    fun discoverPeers() {
        wifiP2pManager!!.discoverPeers(channel, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                mutableState.update { it.copy(fetchingStarted = true) }
            }
            override fun onFailure(reason: Int) {
                mutableState.update { it.copy(fetchingStarted = false) }
            }
        })
    }

    override fun onCleared() {
        channel.also { wifiP2pManager!!.cancelConnect(it, null) }
        super.onCleared()
    }

    companion object{
        private const val TAG: String = "WifiDirectViewModel"
    }
}