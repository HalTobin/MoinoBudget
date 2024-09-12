package org.moineaufactory.moinobudget

import App
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI
import ui.MainViewModel
import ui.theme.MoinoBudgetTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val mainViewModel = koinViewModel<MainViewModel>()
            val preferences by mainViewModel.preferences.collectAsStateWithLifecycle()

            MoinoBudgetTheme(theme = preferences.theme) {
                this@MainActivity.window.statusBarColor = MaterialTheme.colorScheme.background.toArgb()
                DisposableEffect(preferences.theme.isDark) {
                    WindowCompat.getInsetsController(this@MainActivity.window, this@MainActivity.window.decorView).apply {
                        isAppearanceLightStatusBars = !preferences.theme.isDark
                        isAppearanceLightNavigationBars = !preferences.theme.isDark
                    }

                    onDispose {  }
                }
            }

            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}