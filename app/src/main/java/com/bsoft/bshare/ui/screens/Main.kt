package com.bsoft.bshare.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItemColors
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.bsoft.bshare.ui.components.MainTopBar
import com.bsoft.bshare.ui.pages.Files
import com.bsoft.bshare.ui.pages.Home
import com.bsoft.bshare.ui.pages.Settings
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.AdaptiveDestinations
import com.bsoft.bshare.utils.UIPreview

private fun NavigationSuiteScope.navigationItems(colors: NavigationSuiteItemColors, currentPage: AdaptiveDestinations, change: (AdaptiveDestinations)-> Unit){

    AdaptiveDestinations.entries.forEach {
        item(onClick = { change(it) },
            icon = { Image(imageVector = ImageVector.vectorResource(it.icon), contentDescription = "") },
            label = { Text(text = it.title) },
            colors = colors,
            selected = it.title == currentPage.title)
    }
}

@Composable
fun Main(){
    var currentPage by remember { mutableStateOf(AdaptiveDestinations.Home) }
    val colors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    )

    NavigationSuiteScaffold(navigationSuiteItems = { navigationItems(colors = colors, currentPage) { currentPage = it  } }) {
        Scaffold (topBar = { MainTopBar(currentPage) }){
            Surface(modifier = Modifier.padding(paddingValues = it)) {
                when(currentPage){
                    AdaptiveDestinations.Home -> Home()
                    AdaptiveDestinations.Files -> Files()
                    AdaptiveDestinations.Settings -> Settings()
                }
            }
        }
    }
}

@UIPreview
@Composable
fun MainPreview() {
    BshareTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Main()
        }
    }
}