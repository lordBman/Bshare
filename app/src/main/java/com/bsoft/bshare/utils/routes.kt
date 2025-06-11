package com.bsoft.bshare.utils

import com.bsoft.bshare.R

enum class AdaptiveDestinations(val title: String, val icon: Int){
    Home(title = "Home", icon = R.drawable.flat_color_icons__home),
    Files(title = "Files", icon = R.drawable.flat_color_icons__filing_cabinet),
    Settings(title = "Settings", R.drawable.flat_color_icons__settings)
}

enum class RootRoutes(val title: String){
    Main(title = "main"),
    OnBoarding(title = "onboarding"),
    StartUp(title = "startup"),
    Sending(title = "sending"),
    Receiving(title = "receiving")
}