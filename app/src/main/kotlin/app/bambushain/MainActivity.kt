package app.bambushain

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.bambushain.api.AuthenticationApi
import app.bambushain.composables.ForgotPasswordScreen
import app.bambushain.composables.LoginScreen
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

enum class Screens {
    Login,
    ForgotPassword,
    Calendar,
    Pandas,
    Characters,
    MyProfile
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainComposable(
    authenticationApi: AuthenticationApi = koinInject()
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

    var startDestination by remember { mutableStateOf(Screens.Login.name) }

    var checkingToken by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        startDestination = if (authenticationApi.checkToken().isSuccessful) {
            Screens.Calendar.name
        } else {
            Screens.Login.name
        }
        checkingToken = false
    }

    val navHost = @Composable {
        NavHost(
            navController = navController,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            startDestination = startDestination,
        ) {
            composable(Screens.Login.name) {
                LoginScreen(navController)
            }
            composable(Screens.ForgotPassword.name) {
                ForgotPasswordScreen(navController)
            }
            composable(Screens.Calendar.name) { }
            composable(Screens.Pandas.name) { }
            composable(Screens.Characters.name) { }
            composable(Screens.MyProfile.name) { }
        }
    }
    val hideNavigation = setOf(
        Screens.Login.name,
        Screens.ForgotPassword.name
    ).contains(navController.currentDestination?.route ?: "")

    if (!checkingToken) {
        BambooTheme {
            if (hideNavigation) {
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
}