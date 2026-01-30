package com.shuham.ganga

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.shuham.ganga.di.initKoin

fun main() {
    initKoin()
    application {
        Window(
            onCloseRequest = ::exitApplication,
            title = "Ganga",
        ) {
            App()
        }
    }
}