package io.github.hosseinkarami_dev.focusai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import io.github.hosseinkarami_dev.focusai.di.initKoin

class MainActivity : ComponentActivity() {
    private var launchAction by mutableStateOf<String?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initKoin(applicationContext)
        launchAction = intent?.action

        setContent {
            App(initialAction = launchAction, onInitialActionHandled = { launchAction = null })
        }
    }

    override fun onNewIntent(intent: android.content.Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        launchAction = intent.action
    }

    override fun onResume() {
        super.onResume()
        FocusAiWidgetProvider.refresh(applicationContext)
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}
