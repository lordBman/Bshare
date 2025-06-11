package com.bsoft.bshare.ui.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.bsoft.bshare.utils.AdaptiveDestinations

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(currentPage: AdaptiveDestinations){
    val theme = MaterialTheme.colorScheme
    val colors = TopAppBarDefaults.topAppBarColors(containerColor = theme.primary)

    TopAppBar(
        title = { Text(currentPage.title, color = theme.onPrimary) },
        colors = colors)
}