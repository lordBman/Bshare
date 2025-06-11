package com.bsoft.bshare.viewModels

import android.Manifest
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.net.wifi.WifiManager
import android.net.wifi.WpsInfo
import android.net.wifi.p2p.WifiP2pConfig
import android.net.wifi.p2p.WifiP2pDevice
import android.net.wifi.p2p.WifiP2pManager
import android.os.Environment
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.AndroidViewModel
import com.bsoft.bshare.WiFiDirectFileTransferApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.net.ServerSocket
import java.net.Socket
import kotlin.jvm.Throws

private fun transferFile(wifiP2pManager: WifiP2pManager, channel: WifiP2pManager.Channel, file: File, onStatusUpdate: (String) -> Unit) {
    wifiP2pManager.requestConnectionInfo(channel) { info ->
        if (info.groupFormed && info.isGroupOwner) {
            // This device is the group owner (server)
            startFileServer(file, onStatusUpdate)
        } else if (info.groupFormed) {
            // This device is the client
            sendFileToServer(info.groupOwnerAddress.hostAddress!!, file, onStatusUpdate)
        } else {
            onStatusUpdate("Not connected to any group")
        }
    }
}

// Server side (Group Owner)
private fun startFileServer(file: File, onStatusUpdate: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val serverSocket = ServerSocket(8888)
            onStatusUpdate("Server started. Waiting for client...")

            val clientSocket = serverSocket.accept()
            onStatusUpdate("Client connected. Sending file...")

            val outputStream = DataOutputStream(clientSocket.getOutputStream())
            val inputStream = FileInputStream(file)

            // Send file name and size first
            outputStream.writeUTF(file.name)
            outputStream.writeLong(file.length())

            // Send file content
            inputStream.copyTo(outputStream)

            outputStream.close()
            inputStream.close()
            clientSocket.close()
            serverSocket.close()

            onStatusUpdate("File sent successfully")
        } catch (e: Exception) {
            onStatusUpdate("Error: ${e.message}")
        }
    }
}

// Client side
private fun sendFileToServer(serverIp: String, file: File, onStatusUpdate: (String) -> Unit) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val socket = Socket(serverIp, 8888)
            onStatusUpdate("Connected to server. Receiving file...")

            val inputStream = DataInputStream(socket.getInputStream())

            // Read file info
            val fileName = inputStream.readUTF()
            val fileSize = inputStream.readLong()

            // Create file
            val receivedFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName)
            val fileOutputStream = FileOutputStream(receivedFile)

            // Receive file content
            var bytesRead: Int
            val buffer = ByteArray(1024)
            var totalBytesRead: Long = 0

            while (totalBytesRead < fileSize) {
                bytesRead = inputStream.read(buffer)
                if (bytesRead == -1) break
                fileOutputStream.write(buffer, 0, bytesRead)
                totalBytesRead += bytesRead
            }

            fileOutputStream.close()
            inputStream.close()
            socket.close()

            onStatusUpdate("File received: $fileName")
        } catch (e: Exception) {
            onStatusUpdate("Error: ${e.message}")
        }
    }
}

data class WifiState(
    val contentedDevice: WifiP2pDevice? = null,
    val devices: List<WifiP2pDevice> = listOf(),
    val isError: Boolean = false,
    val enabled: Boolean = false,
    val connected: Boolean = false,
    val fetchingStarted: Boolean = false,
    val message: String = "")

class WifiDirectViewModel(application: Application) : AndroidViewModel(application) {
    private val app = application.applicationContext as WiFiDirectFileTransferApp
    private val wifiP2pManager = app.wifiP2pManager
    private val channel = app.channel

    private val mutableState = MutableStateFlow(WifiState())
    val state: StateFlow<WifiState> = mutableState.asStateFlow()

    private val connectivityManager by lazy {
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            val caps = connectivityManager.getNetworkCapabilities(network)
            if (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)) {
                mutableState.update { it.copy(connected = true) }
            }
        }

        @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
        override fun onLost(network: Network) {
            super.onLost(network)
            val caps = connectivityManager.getNetworkCapabilities(network)
            if (caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_WIFI_P2P)) {
                mutableState.update { it.copy(connected = false) }
            }
        }
    }

    private val peerListListener = WifiP2pManager.PeerListListener { peerList ->
        mutableState.update { it.copy(devices = peerList.deviceList.toList()) }
    }

    private val receiver = object : BroadcastReceiver() {
        @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES, Manifest.permission.ACCESS_NETWORK_STATE])
        override fun onReceive(context: Context, intent: Intent) {
            when (intent.action) {
                WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION -> {
                    val state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1)
                    mutableState.update {
                        it.copy(enabled = state == WifiP2pManager.WIFI_P2P_STATE_ENABLED)
                    }
                }
                WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION -> wifiP2pManager!!.requestPeers(channel, peerListListener)
                WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION -> {
                    connectivityManager.registerNetworkCallback(
                        NetworkRequest.Builder().addTransportType(NetworkCapabilities.TRANSPORT_WIFI).build(),
                        networkCallback
                    )
                }
            }
        }
    }

    @Throws(Exception::class)
    fun registerReceiver(context: Context){
        if(channel != null || wifiP2pManager != null){
            val intentFilter = IntentFilter().apply {
                addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION)
                addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION)
            }
            context.registerReceiver(receiver, intentFilter)
        }else{
            throw Exception("wifi is either switched off or not wifi direct is not supported")
        }
    }

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.NEARBY_WIFI_DEVICES])
    fun connect(device: WifiP2pDevice) {
        val config = WifiP2pConfig().apply {
            deviceAddress = device.deviceAddress
            wps.setup = WpsInfo.PBC
        }
        mutableState.update { it.copy(message = "connecting to ${device.deviceName}") }
        wifiP2pManager!!.connect(channel, config, object : WifiP2pManager.ActionListener {
            override fun onSuccess() {
                mutableState.update { it.copy(contentedDevice = device) }
            }
            override fun onFailure(reason: Int) {
                mutableState.update { it.copy(message = "failed connecting to ${device.deviceName}") }
            }
        })
    }

    fun unregisterReceiver(context: Context) {
        try {
            context.unregisterReceiver(receiver)
        } catch (e: IllegalArgumentException) {
            // Receiver was not registered
        }
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