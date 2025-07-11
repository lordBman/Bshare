package com.bsoft.bshare.services

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import android.os.Environment
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.InetSocketAddress
import java.net.Socket

class FileService : Service() {
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

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent.let {
            when(it?.action){
                Actions.START_SERVICE.toString() -> {
                    it.getStringExtra("groupIPAddress").let {
                        groupIPAddress -> start(groupIPAddress!!)
                    }
                }
                Actions.STOP_SERVICE.toString() -> stopSelf()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    fun listenForIncomingFile(socket: Socket) {
        val inputStream = DataInputStream(socket.getInputStream())
        val outputStream = DataOutputStream(socket.getOutputStream())


    }

    @SuppressLint("ForegroundServiceType")
    private fun start(groupIPAddress: String){
        val socket = Socket()
        socket.bind(null)
        socket.connect(InetSocketAddress(groupIPAddress, 8888), 5000)

        val notification = NotificationCompat.Builder(this, FileServiceStartNotification).build()

        startForeground(1, notification)

        socket.use {
            listenForIncomingFile(it)
        }
    }

    enum class Actions{
        START_SERVICE, STOP_SERVICE
    }

    companion object{
        val FileServiceStartNotification = "File Service Start Notification"
    }
}