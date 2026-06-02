package app.bambushain.composables

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import app.bambushain.Screens
import app.bambushain.api.AuthenticationApi
import app.bambushain.model.Login
import app.bambushain.model.LoginResult
import app.bambushain.setBambooToken
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private fun storeLoginResult(result: LoginResult, context: Context) {
    context.setBambooToken(result.token!!)
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

    val login = {
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
            Column(
                modifier = Modifier.padding(16.dp),
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
                )
                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Passwort") },
                    value = password,
                    onValueChange = { password = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                )
                if (loginInProgress) {
                    TextField(
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Zwei Faktor Code") },
                        value = twoFactorCode,
                        onValueChange = { twoFactorCode = it },
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
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
                        FilledTonalButton(onClick = {}) {
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