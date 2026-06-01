package app.bambushain

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.bambushain.theme.BambooTheme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val eventReminder = NotificationChannel(
            "event-reminder",
            "Erinnerungen über Events",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        eventReminder.description = "Benachrichtigung wenn ein Event ansteht"
        eventReminder.enableVibration(true)

        val groveInvites = NotificationChannel(
            "grove-invites",
            "Änderung der Einladungen",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        groveInvites.description =
            "Benachrichtigung wenn die Einladungen aktiviert oder deaktiviert werden"
        groveInvites.enableVibration(true)

        val notificationManager = getSystemService(NotificationManager::class.java)
        notificationManager.createNotificationChannels(listOf(eventReminder, groveInvites))

        setContent {
            MainComposable()
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainComposable(
    context: Context = koinInject(),
) {
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()

    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack?.destination?.route

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val permissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
        LaunchedEffect(Unit) {
            if (!permissionState.status.isGranted) {
                permissionState.launchPermissionRequest()
            }
        }
    }

    val navHost = @Composable {
        NavHost(
            navController = navController,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            startDestination = "",
        ) {
        }
    }
    val showNavigation = setOf("").contains(navController.currentDestination ?: "")

    BambooTheme {
        if (showNavigation) {
            navHost()
        } else {
            NavigationSuiteScaffold(
                navigationItems = {
                }
            ) {
                navHost()
            }
        }
    }
}