package com.bsoft.bshare.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.UIPreview
import com.bsoft.bshare.viewModels.WifiDirectViewModel

@Composable
fun OnBoarding(viewModel: WifiDirectViewModel = viewModel()){
    val context = LocalContext.current
    val wifiState by viewModel.state.collectAsState()

    fun search(){
        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                context, Manifest.permission.NEARBY_WIFI_DEVICES) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        viewModel.discoverPeers()
    }

    Scaffold { screenPadding ->
        Surface(modifier = Modifier.padding(screenPadding)){
            Column {
                Text(if(wifiState.enabled){ "Wifi P2P enabled" } else { "Wifi P2P disabled" })
                Text(if(wifiState.connected){ "Wifi P2P connected" } else { "Wifi P2P disconnected" })
                Text(if(wifiState.fetchingStarted){ "searching.." } else { "idle" })
                Text(text = wifiState.message)
                Text("devices")
                wifiState.devices.forEach { device ->
                    Row {
                        Text("${device.deviceName}: ${device.deviceAddress}")
                        Button(onClick = { viewModel.connect(device) }) {
                            Text(text = "connect")
                        }
                    }
                }
                Button(onClick = { search() }) {
                    Text("find devices")
                }
            }
        }
    }
}

@UIPreview
@Composable
private fun OnBoardingPreview(){
    BshareTheme {
        OnBoarding()
    }
}