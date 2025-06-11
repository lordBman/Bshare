package com.bsoft.bshare.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.bshare.R
import com.bsoft.bshare.ui.components.BShareTopBar
import com.bsoft.bshare.ui.components.OptionsMenu
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.UIPreview

@Composable
fun StartUp(){
    //val navController = LocalNavController.current
    val createAnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Create: ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.W200, letterSpacing = 1.5.sp)) {
            append("Create a wifi network, so your friends can join to facilitate sharing. this is recommended than using a public wifi")
        }
    }

    val joinAnnotatedString = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append("Join: ")
        }
        withStyle(style = SpanStyle(fontWeight = FontWeight.W200, letterSpacing = 1.5.sp)) {
            append("Join a wifi network, both you and your friend has to be connected to the same wifi network. the recommended network is the on created on BShare")
        }
    }

    Scaffold(topBar = {
        BShareTopBar( title = "Wifi Connection", icon = ImageVector.vectorResource(R.drawable.clarity__wifi_line))
    }) { innerPadding ->
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize().padding(innerPadding)) {
            Text("Connect to a Wifi Network", fontWeight = FontWeight.ExtraBold, lineHeight = 42.sp, fontSize = 36.sp, modifier = Modifier.width(300.dp).padding(start = 20.dp, top = 40.dp))
            Box (contentAlignment = Alignment.Center, modifier = Modifier.weight(1f)){
                OptionsMenu(modifier = Modifier.fillMaxWidth()){
                    item(icon = R.drawable.emojione__satellite, label = "Create", pressed = {})
                    item(icon = R.drawable.emojione__satellite_antenna, label = "Join", pressed = {})
                }
            }
            Column(modifier = Modifier.padding(20.dp)) {
                Text(text = createAnnotatedString)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = joinAnnotatedString)
            }
        }
    }
}

@UIPreview
@Composable
private fun StartUpPreview(){
    BshareTheme {
        StartUp()
    }
}
