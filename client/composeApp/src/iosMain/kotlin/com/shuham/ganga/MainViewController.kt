package com.shuham.ganga

import androidx.compose.ui.window.ComposeUIViewController
import com.shuham.ganga.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) { App() }