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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteItem
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.bambushain.api.AuthenticationApi
import app.bambushain.composables.Calendar
import app.bambushain.composables.ForgotPasswordScreen
import app.bambushain.composables.LoginScreen
import app.bambushain.composables.Pandas
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
    FinalFantasy
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MainComposable(
    authenticationApi: AuthenticationApi = koinInject()
) {
    val navController = rememberNavController()

    val currentBackStack by navController.currentBackStackEntryAsState()

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
    var fabClicked by remember { mutableStateOf(false) }

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
            composable(Screens.Calendar.name) {
                Calendar(
                    fabClicked,
                    { fabClicked = false }
                )
            }
            composable(Screens.Pandas.name) {
                Pandas()
            }
            composable(Screens.FinalFantasy.name) { }
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
                    navigationItemVerticalArrangement = Arrangement.Center,
                    navigationItems = {
                        NavigationSuiteItem(
                            selected = navController.currentDestination?.route == Screens.Calendar.name,
                            onClick = { navController.navigate(Screens.Calendar.name) },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.calendar_month),
                                    contentDescription = "Kalender"
                                )
                            },
                            label = { Text("Kalender") },
                        )
                        NavigationSuiteItem(
                            selected = navController.currentDestination?.route == Screens.Pandas.name,
                            onClick = { navController.navigate(Screens.Pandas.name) },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.account_group),
                                    contentDescription = "Pandas"
                                )
                            },
                            label = { Text("Pandas") },
                        )
                        NavigationSuiteItem(
                            selected = navController.currentDestination?.route == Screens.FinalFantasy.name,
                            onClick = { navController.navigate(Screens.FinalFantasy.name) },
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.final_fantasy),
                                    contentDescription = "Final Fantasy"
                                )
                            },
                            label = { Text("Final Fantasy") },
                        )
                    },
                    primaryActionContent = {
                        if (navController.currentDestination?.route in setOf(
                                Screens.Calendar.name,
                                Screens.FinalFantasy.name
                            )
                        ) {
                            FloatingActionButton(
                                modifier = Modifier.padding(start = 20.dp),
                                onClick = {
                                    fabClicked = true
                                },
                            ) {
                                if (Screens.Calendar.name == navController.currentDestination?.route) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.calendar_plus),
                                        contentDescription = "Event hinzufügen"
                                    )
                                } else if (Screens.FinalFantasy.name == navController.currentDestination?.route) {
                                    Icon(
                                        ImageVector.vectorResource(R.drawable.plus),
                                        contentDescription = "Charakter hinzufügen"
                                    )
                                }
                            }
                        }
                    },
                ) {
                    navHost()
                }
            }
        }
    }
}