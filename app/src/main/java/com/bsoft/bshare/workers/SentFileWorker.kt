package com.bsoft.bshare.workers

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.ServerSocket

class SentFileWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        return try {
            val fileName = inputData.getString("fileName")
            val fileUri = inputData.getString("fileUri")

            if (fileName != null && fileUri != null) {
                uploadFile(fileName, fileUri) { status ->
                    Log.d(TAG, status)
                }
                Result.success()
            }
            Result.failure()
        }catch (ex: Exception){
            Result.failure()
        }
    }

    private suspend fun uploadFile( fileName: String, fileUri: String, onStatusUpdate: (String) -> Unit){
        try {
            val result = withContext(Dispatchers.IO) {
                val clientSocket = ServerSocket(8888).run {
                    onStatusUpdate("Server started. Waiting for client...")
                    accept()
                }
                clientSocket.use {
                    onStatusUpdate("Client connected. Sending ${fileName}...")
                    val inputStream = applicationContext.contentResolver.openInputStream(fileUri.toUri())
                    val outputStream = DataOutputStream(it.getOutputStream())

                    if (inputStream != null) {
                        copyFile(inputStream, outputStream)
                    }
                    false
                }
            }

            if(result){
                onStatusUpdate("File sent successfully")
            }else{
                onStatusUpdate("Error sending file")
            }
        } catch (e: Exception) {
            onStatusUpdate("Error: ${e.message}")
        }
    }

    private fun copyFile(inputStream: InputStream, out: OutputStream): Boolean {
        val buf = ByteArray(1024)
        var len: Int
        try {
            while ((inputStream.read(buf).also { len = it }) != -1) {
                out.write(buf, 0, len)
            }
        } catch (e: IOException) {
            Log.d(TAG, e.toString())
            return false
        }
        return true
    }

    companion object{
        private const val TAG = "SentFileWorker"
    }
}