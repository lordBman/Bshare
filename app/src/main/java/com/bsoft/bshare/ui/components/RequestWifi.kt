package com.bsoft.bshare.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.bshare.R
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.UIPreview

@Composable
fun RequestWifi(){
    val theme = MaterialTheme.colorScheme

    Surface(modifier = Modifier.fillMaxWidth(), color = Color.Transparent) {
        Box(modifier = Modifier.padding(vertical = 10.dp), contentAlignment = Alignment.Center){
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(6.dp)){
                AnimatedImage(R.raw.wifi_lost, modifier = Modifier.size(width = 200.dp, height = 120.dp))
                Spacer(Modifier.height(40.dp))
                Text(text = "Wifi is not enabled", fontSize = 20.sp, color = theme.primary, fontWeight = FontWeight.Bold)
                Text(text = "Please enable wifi to continue", fontSize = 14.sp, fontWeight = FontWeight.ExtraLight, letterSpacing = 1.5.sp)
            }
        }
    }
}

@UIPreview
@Composable
private fun RequestWifiPreview(){
    BshareTheme{
        RequestWifi()
    }
}