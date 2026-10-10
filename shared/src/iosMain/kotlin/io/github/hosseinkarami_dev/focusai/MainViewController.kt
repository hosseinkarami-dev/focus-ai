package io.github.hosseinkarami_dev.focusai

import androidx.compose.ui.window.ComposeUIViewController
import io.github.hosseinkarami_dev.focusai.di.initKoin

fun MainViewController() = run {
    initKoin()
    ComposeUIViewController { App() }
}
