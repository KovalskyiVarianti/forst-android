package com.example.forst_android.main.ui

sealed interface MainEntryPoint {
    object Splash : MainEntryPoint
    object Login : MainEntryPoint
    object Home : MainEntryPoint
}