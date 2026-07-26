package com.gentech.chipcheck.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gentech.chipcheck.domain.repository.ScanSource
import com.gentech.chipcheck.ui.history.HistoryScreen
import com.gentech.chipcheck.ui.manualentry.ManualEntryScreen
import com.gentech.chipcheck.ui.reference.ReferenceScreen
import com.gentech.chipcheck.ui.result.ResultScreen
import com.gentech.chipcheck.ui.scan.ScanScreen
import com.gentech.chipcheck.ui.settings.SettingsScreen

private data class BottomTab(val route: String, val label: String, val icon: ImageVector)

private val bottomTabs = listOf(
    BottomTab(Routes.SCAN, "Scan", Icons.Filled.CameraAlt),
    BottomTab(Routes.HISTORY, "History", Icons.Filled.History),
    BottomTab(Routes.REFERENCE, "Reference", Icons.AutoMirrored.Filled.MenuBook),
    BottomTab(Routes.SETTINGS, "Settings", Icons.Filled.Settings)
)

@Composable
fun ChipCheckNavHost(navController: NavHostController = rememberNavController()) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    Scaffold(
        bottomBar = {
            if (bottomTabs.any { it.route == currentRoute }) {
                NavigationBar {
                    bottomTabs.forEach { tab ->
                        NavigationBarItem(
                            selected = currentRoute == tab.route,
                            onClick = {
                                navController.navigate(tab.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            icon = { Icon(tab.icon, contentDescription = tab.label) },
                            label = { Text(tab.label) }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.SCAN,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.SCAN) {
                ScanScreen(
                    onPartNumberChosen = { partNumber ->
                        navController.navigate(Routes.resultForPartNumber(partNumber, ScanSource.CAMERA_OCR))
                    },
                    onManualEntryRequested = { navController.navigate(Routes.MANUAL_ENTRY) }
                )
            }
            composable(Routes.MANUAL_ENTRY) {
                ManualEntryScreen(
                    onViewResult = { partNumber ->
                        navController.navigate(Routes.resultForPartNumber(partNumber, ScanSource.MANUAL))
                    }
                )
            }
            composable(
                route = Routes.RESULT_ROUTE,
                arguments = listOf(
                    navArgument(Routes.RESULT_PART_NUMBER_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument(Routes.RESULT_HISTORY_ID_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    },
                    navArgument(Routes.RESULT_SOURCE_ARG) {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = ScanSource.MANUAL.name
                    }
                )
            ) {
                ResultScreen(onBack = { navController.popBackStack() })
            }
            composable(Routes.HISTORY) {
                HistoryScreen(
                    onItemSelected = { historyId ->
                        navController.navigate(Routes.resultForHistoryId(historyId))
                    }
                )
            }
            composable(Routes.REFERENCE) {
                ReferenceScreen()
            }
            composable(Routes.SETTINGS) {
                SettingsScreen()
            }
        }
    }
}
