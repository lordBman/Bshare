package com.bsoft.bshare.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bsoft.bshare.ui.LocalNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BShareTopBar(title: String, icon: ImageVector){
    val navController = LocalNavController.current

    TopAppBar(
        title = { Text(text = title, color = MaterialTheme.colorScheme.onPrimary, fontSize = 16.sp) },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                }
            }
            Icon(imageVector = icon, contentDescription = "", modifier = Modifier.size(24.dp)) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            navigationIconContentColor =  MaterialTheme.colorScheme.onPrimary))
}