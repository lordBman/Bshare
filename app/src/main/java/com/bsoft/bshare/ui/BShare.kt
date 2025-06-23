package com.bsoft.bshare.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalBottomSheetProperties
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.bsoft.bshare.ui.components.RequestWifi
import com.bsoft.bshare.ui.screens.Main
import com.bsoft.bshare.ui.screens.OnBoarding
import com.bsoft.bshare.ui.screens.Receiving
import com.bsoft.bshare.ui.screens.Sending
import com.bsoft.bshare.ui.screens.StartUp
import com.bsoft.bshare.ui.theme.BshareTheme
import com.bsoft.bshare.utils.RootRoutes
import com.bsoft.bshare.utils.UIPreview
import com.bsoft.bshare.viewModels.WifiDirectViewModel

val LocalNavController = compositionLocalOf<NavHostController> { error("No NavHostController provided") }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BShare(navController: NavHostController = rememberNavController(), viewModel: WifiDirectViewModel = viewModel()){
    val context = LocalContext.current
    val wifiState by viewModel.state.collectAsState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // Handle receiver registration/unregistration
    DisposableEffect(Unit) {
        viewModel.registerReceiver(context)

        onDispose {
            viewModel.unregisterReceiver(context)
        }
    }

    Surface(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalNavController provides navController) {
            NavHost(navController = navController, startDestination = RootRoutes.OnBoarding.title, modifier = Modifier.fillMaxSize()) {
                composable(route = RootRoutes.StartUp.title) { StartUp() }
                composable(route = RootRoutes.Main.title) { Main() }
                composable(route = RootRoutes.Receiving.title) { Receiving() }
                composable(route = RootRoutes.Sending.title) { Sending() }
                composable(route = RootRoutes.OnBoarding.title) { OnBoarding(viewModel = viewModel) }
            }
        }
    }

    // check if wifi is enabled and show Modelbottomsheet to ask for user to turn on Wifi
    if(!wifiState.enabled){
        // show Modelbottomsheet to ask for user to turn on Wifi
        ModalBottomSheet(modifier = Modifier.fillMaxWidth(),
            properties = ModalBottomSheetProperties(shouldDismissOnBackPress = false),
            sheetState = sheetState,onDismissRequest = {}) {
            RequestWifi()
        }
    }

}

@UIPreview
@Composable
private fun BSharePreview(){
    BshareTheme {
        BShare()
    }
}