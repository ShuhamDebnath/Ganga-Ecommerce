package com.shuham.ganga

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.shuham.ganga.di.initKoin

fun main() {
    try {
        initKoin()
    } catch (e: Exception) {
        // Koin already started, ignore
    }
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Ganga",
        ) {
            App()
        }
    }
}