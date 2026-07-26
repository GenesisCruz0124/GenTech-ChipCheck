package com.gentech.chipcheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.gentech.chipcheck.domain.repository.ThemeMode
import com.gentech.chipcheck.ui.navigation.ChipCheckNavHost
import com.gentech.chipcheck.ui.settings.SettingsViewModel
import com.gentech.chipcheck.ui.theme.ChipCheckTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChipCheckRoot()
        }
    }
}

@Composable
private fun ChipCheckRoot(settingsViewModel: SettingsViewModel = hiltViewModel()) {
    val themeMode by settingsViewModel.themeMode.collectAsStateWithLifecycle()
    ChipCheckTheme(darkTheme = themeMode.resolveIsDark()) {
        Surface(modifier = Modifier.fillMaxSize()) {
            ChipCheckNavHost()
        }
    }
}

@Composable
private fun ThemeMode.resolveIsDark(): Boolean = when (this) {
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
    ThemeMode.SYSTEM -> androidx.compose.foundation.isSystemInDarkTheme()
}
