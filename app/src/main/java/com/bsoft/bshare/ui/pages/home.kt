package com.bsoft.bshare.ui.pages

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.bsoft.bshare.R
import com.bsoft.bshare.ui.components.AnimatedImage
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.UIPreview


@Composable
fun Home(){
    Surface{
        Box (contentAlignment = Alignment.Center){
            AnimatedImage(lottieResource = R.raw.wifi_searching)
        }
    }
}

@UIPreview
@Composable
private fun HomePreview(){
    BshareTheme {
        Home()
    }
}