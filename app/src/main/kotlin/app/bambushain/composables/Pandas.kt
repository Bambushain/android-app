package app.bambushain.composables


import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.bambushain.R
import app.bambushain.api.GrovesApi
import app.bambushain.api.PandasApi
import app.bambushain.getBambooToken
import app.bambushain.model.Grove
import app.bambushain.model.WebUser
import coil3.compose.AsyncImage
import coil3.network.NetworkHeaders
import coil3.network.httpHeaders
import coil3.request.ImageRequest
import coil3.request.crossfade
import org.koin.compose.koinInject
import app.bambushain.api.R as apiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Pandas(
    pandasApi: PandasApi = koinInject(),
    grovesApi: GrovesApi = koinInject()
) {
    var groveId by remember { mutableStateOf<Int?>(null) }

    var groves by remember { mutableStateOf(emptyList<Grove>()) }

    var pandas by remember { mutableStateOf(emptyList<WebUser>()) }

    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(groveId) {
        val res = pandasApi.getUsers(groveId)
        if (res.isSuccessful) {
            pandas = res.body() ?: emptyList()
        } else {
            Log.e("Pandas", res.errorBody()?.string() ?: res.code().toString())
        }
    }
    LaunchedEffect(Unit) {
        groves = grovesApi.getGroves().body() ?: emptyList()
    }

    val authHeaders = remember {
        NetworkHeaders.Builder()
            .set("Authorization", "Bearer ${context.getBambooToken()}")
            .build()
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text("Pandas")
                },
                actions = {
                    if (groves.isNotEmpty()) {
                        Box {
                            var showMore by remember {
                                mutableStateOf(
                                    false
                                )
                            }
                            IconButton(onClick = {
                                showMore = true
                            }) {
                                BadgedBox(
                                    badge = {
                                        if (groveId != null) {
                                            Badge(containerColor = MaterialTheme.colorScheme.primary)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = ImageVector.vectorResource(R.drawable.filter_variant),
                                        contentDescription = "Nach Hain filtern"
                                    )
                                }
                            }
                            DropdownMenu(
                                expanded = showMore,
                                onDismissRequest = { showMore = false }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            "Alle Haine",
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                        )
                                    },
                                    onClick = {
                                        groveId = null
                                        showMore = false
                                    }
                                )
                                for (grove in groves) {
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                grove.name,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                            )
                                        },
                                        onClick = {
                                            groveId = grove.id
                                            showMore = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            )
        }
    ) { scaffoldPadding ->
        Surface(
            modifier = Modifier
                .padding(scaffoldPadding)
                .fillMaxSize()
        ) {

            LazyVerticalStaggeredGrid(
                modifier = Modifier
                    .fillMaxSize(),
                columns = StaggeredGridCells.Adaptive(480.dp),
                contentPadding = PaddingValues(16.dp),
                verticalItemSpacing = 16.dp,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(pandas) {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(stringResource(apiR.string.server) + "/api/user/${it.id}/picture")
                                    .httpHeaders(authHeaders)
                                    .crossfade(true)
                                    .build(),
                                contentDescription = null,
                                contentScale = ContentScale.Crop,
                                fallback = painterResource(R.drawable.spring),
                                modifier = Modifier
                                    .width(128.dp)
                                    .height(128.dp),
                            )
                            Column(
                                modifier = Modifier
                                    .padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(it.displayName, style = MaterialTheme.typography.titleMedium)
                                Text(it.email)
                                if (it.discordName.isNotEmpty()) {
                                    Text("Auf Discord bekannt als " + it.discordName)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
