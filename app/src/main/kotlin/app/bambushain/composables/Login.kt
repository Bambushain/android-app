package app.bambushain.composables

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import app.bambushain.Screens
import app.bambushain.api.AuthenticationApi
import app.bambushain.model.ForgotPassword
import app.bambushain.model.Login
import app.bambushain.model.LoginResult
import app.bambushain.setBambooToken
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import kotlin.time.Clock

private fun storeLoginResult(result: LoginResult, context: Context) {
    context.setBambooToken(result.token)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    navController: NavController,
    authenticationApi: AuthenticationApi = koinInject()
) {
    var email by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    val requestLink = {
        coroutineScope.launch {
            val response = authenticationApi.forgotPassword(ForgotPassword(email))
            if (response.isSuccessful) {
                Toast.makeText(
                    context,
                    "Wenn wir einen Account mit deinen Daten haben wird dir ein Link zugeschickt",
                    Toast.LENGTH_LONG
                ).show()
                navController.navigate(Screens.Login.name)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Passwort vergessen") }
            )
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                Panda(modifier = Modifier.align(Alignment.Top), date = today)
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Gib unten deine Email oder Benutzernamen ein und dir wird ein Link zugeschickt",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        value = email,
                        onValueChange = { email = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions(
                            {
                                requestLink()
                            }
                        ),
                    )
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(onClick = {
                            requestLink()
                        }) {
                            Text("Link anfordern")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    authenticationApi: AuthenticationApi = koinInject()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var twoFactorCode by remember { mutableStateOf("") }

    var loginInProgress by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current;
    val focusManager = LocalFocusManager.current

    val login = {
        focusManager.clearFocus()
        coroutineScope.launch {
            if (!loginInProgress) {
                val result = authenticationApi.login(Login(email, password))
                if (result.code() == 204) {
                    loginInProgress = true
                } else if (!result.isSuccessful) {
                    snackbarHostState.showSnackbar("Für die Anmeldedaten wurde kein Benutzer gefunden")
                } else {
                    storeLoginResult(result.body()!!, context)
                    navController.navigate(Screens.Calendar.name)
                }
            } else {
                val result = authenticationApi.login(
                    Login(
                        email,
                        password,
                        twoFactorCode
                    )
                )
                if (!result.isSuccessful) {
                    snackbarHostState.showSnackbar("Der Zwei Faktor Code ist falsch")
                } else {
                    storeLoginResult(result.body()!!, context)
                    navController.navigate(Screens.Calendar.name)
                }
            }
        }
    }

    val today = remember {
        Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Anmelden") }
            )
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
            ) {
                Panda(modifier = Modifier.align(Alignment.Top), date = today)
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        "Melde dich an und betrete den Bambushain",
                        style = MaterialTheme.typography.headlineSmall
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Email") },
                        value = email,
                        onValueChange = { email = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                        keyboardActions = KeyboardActions({
                            focusManager.moveFocus(
                                FocusDirection.Down
                            )
                        }),
                        singleLine = true
                    )
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Passwort") },
                        value = password,
                        onValueChange = { password = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                        visualTransformation = PasswordVisualTransformation(),
                        keyboardActions = KeyboardActions({
                            if (!loginInProgress) {
                                login()
                            } else {
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        }),
                        singleLine = true
                    )
                    if (loginInProgress) {
                        TextField(
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text("Zwei Faktor Code") },
                            value = twoFactorCode,
                            onValueChange = { twoFactorCode = it },
                            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                            keyboardActions = KeyboardActions(
                                {
                                    if (loginInProgress) {
                                        login()
                                    }
                                }
                            ),
                            singleLine = true
                        )
                    }
                    Row(
                        horizontalArrangement =
                            if (loginInProgress) {
                                Arrangement.End
                            } else {
                                Arrangement.SpaceBetween
                            },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        if (!loginInProgress) {
                            FilledTonalButton(onClick = {
                                navController.navigate(Screens.ForgotPassword.name)
                            }) {
                                Text("Passwort vergessen")
                            }
                        }
                        Button(onClick = {
                            login()
                        }) {
                            Text("Anmelden")
                        }
                    }
                }
            }
        }
    }
}