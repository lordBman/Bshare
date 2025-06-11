package com.bsoft.bshare.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.bshare.R
import com.bsoft.bshare.ui.theme.BshareTheme

@DslMarker
annotation class OptionsMenuDsl

@OptionsMenuDsl
class OptionsMenuScope{
    private val items = mutableListOf<@Composable () -> Unit>()

    fun item(icon: Int, label: String, pressed: () -> Unit){
        items.add{
            Item(icon = icon, label = label, pressed = pressed)
        }
    }

    internal fun renderItems() = items
}

@Composable
private fun Item(icon: Int, label: String, pressed: () -> Unit){
    val colors = ButtonDefaults.buttonColors( containerColor = MaterialTheme.colorScheme.surfaceContainer, contentColor = MaterialTheme.colorScheme.onSurface);

    Button (onClick = pressed, colors = colors, shape = CircleShape, modifier = Modifier.size(120.dp)){
        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(10.dp)) {
            Image(imageVector = ImageVector.vectorResource(icon), contentDescription = "", modifier = Modifier.size(54.dp))
            Spacer(modifier = Modifier.height(4.dp))
            Text(label, fontSize = 14.sp)
        }
    }
}


@Composable
fun OptionsMenu(modifier: Modifier = Modifier, content:  OptionsMenuScope.() -> Unit){
    val scope = OptionsMenuScope().apply(content)

    Row(horizontalArrangement = Arrangement.SpaceAround, modifier = modifier){
        scope.renderItems().forEach { itemContent ->
            itemContent()
        }
    }
}

@Preview(showBackground = true, name = "Light Mode Preview", device = "id:pixel_4", apiLevel = 33)
@Preview(showBackground = true, name = "Dark Mode Preview ", uiMode = Configuration.UI_MODE_NIGHT_YES, device = "id:pixel_4", apiLevel = 33)
@Composable
fun OptionsMenuPreview(){
    BshareTheme {
        OptionsMenu(modifier = Modifier.fillMaxWidth()){
            item(icon = R.drawable.noto_v1__bell, label = "Send", pressed = {})
            item(icon = R.drawable.noto_v1__open_mailbox_with_raised_flag, label = "Receive", pressed = {})
        }
    }
}