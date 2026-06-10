package app.bambushain.composables.calendar

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import app.bambushain.R
import app.bambushain.api.CalendarApi
import app.bambushain.api.GrovesApi
import app.bambushain.composables.Panda
import app.bambushain.getBambooToken
import app.bambushain.model.Grove
import app.bambushain.model.GroveEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.sse.SSE
import io.ktor.client.plugins.sse.deserialize
import io.ktor.client.plugins.sse.sse
import io.ktor.client.request.header
import io.ktor.client.request.url
import io.ktor.sse.TypedServerSentEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.koin.compose.koinInject
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import app.bambushain.api.R as apiR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Calendar(
    fabClicked: Boolean,
    onFabFinished: () -> Unit,
    calendarApi: CalendarApi = koinInject(),
    grovesApi: GrovesApi = koinInject()
) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    var events by remember { mutableStateOf(emptyList<GroveEvent>()) }
    var groveId by remember { mutableStateOf<Int?>(null) }
    var reloadTrigger by remember { mutableStateOf(true) }
    var groves by remember { mutableStateOf(emptyList<Grove>()) }

    val server = stringResource(apiR.string.server)

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }

    val days = remember {
        (-3650..3650).map { today.plus(it, DateTimeUnit.DAY) }
    }
    val agendaState = rememberLazyListState(days.indexOfFirst { it == today })
    val visibleMonth by remember {
        derivedStateOf {
            days.getOrNull(agendaState.firstVisibleItemIndex)
        }
    }
    val currentVisibleMonth by remember {
        derivedStateOf {
            visibleMonth?.format(
                LocalDate.Format {
                    monthName(germanMonthNames)
                    chars(" ")
                    year()
                }
            ) ?: ""
        }
    }
    var startDate by remember {
        mutableStateOf(visibleMonth?.minus(DatePeriod(0, 3, 0)))
    }
    var endDate by remember {
        mutableStateOf(visibleMonth?.plus(DatePeriod(0, 3, 0)))
    }

    val jumpToToday = {
        coroutineScope.launch {
            agendaState.scrollToItem(days.indexOfFirst { it == today })
        }
    }

    val deleteEvent = { event: GroveEvent ->
        coroutineScope.launch {
            val response = calendarApi.deleteGroveEvent(event.id)
            if (!response.isSuccessful) {
                snackbarHostState.showSnackbar("Das Event konnte leider nicht gelöscht werden")
            }
        }
    }

    DisposableEffect(Unit) {
        val client = HttpClient {
            install(SSE) {
                maxReconnectionAttempts = 0
                reconnectionTime = 2.seconds
            }
        }

        val json = Json { ignoreUnknownKeys = true }
        val server =
            server.toHttpUrlOrNull()!!.newBuilder().addPathSegments("/sse/event").build()

        val job = coroutineScope.launch {
            while (isActive) {
                try {
                    client.sse({
                        url(server.toString())
                        header("Authorization", "Bearer ${context.getBambooToken()}")
                    }, deserialize = { typeInfo, jsonString ->
                        val serializer =
                            json.serializersModule.serializer(typeInfo.kotlinType!!)
                        json.decodeFromString(serializer, jsonString)!!
                    }) {
                        incoming.collect { event: TypedServerSentEvent<String> ->
                            val data = deserialize<GroveEvent>(event.data)
                            try {
                                if (data != null && startDate != null && endDate != null &&
                                    (data.startDate >= startDate!! || data.endDate <= endDate!!)
                                ) {
                                    reloadTrigger = true
                                }
                            } catch (ex: Throwable) {
                                Log.e("Calender", "Failed to decode event: $data", ex)
                            }
                        }
                    }
                } catch (ex: CancellationException) {
                    throw ex
                } catch (ex: Throwable) {
                    Log.e("Calender", "SSE connection dropped, reconnecting...", ex)
                    delay(2.seconds)
                }
            }
        }

        onDispose {
            job.cancel()
            client.close()
        }
    }
    LaunchedEffect(Unit) {
        groves = grovesApi.getGroves().body() ?: emptyList()
    }
    LaunchedEffect(visibleMonth) {
        startDate = visibleMonth?.minus(DatePeriod(0, 3, 0))
        endDate = visibleMonth?.plus(DatePeriod(0, 3, 0))
    }
    LaunchedEffect(groveId, startDate, endDate, reloadTrigger) {
        val response =
            calendarApi.getGroveEvents(startDate ?: today, endDate ?: today, groveId)
        if (response.isSuccessful) {
            events = response.body() ?: emptyList()
            reloadTrigger = false
        }
    }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        buildString {
                            append(currentVisibleMonth)
                            if (groveId != null) {
                                val grove = groves.find { it.id == groveId }
                                if (grove != null) {
                                    append(" (auf ${grove.name} gefiltert)")
                                }
                            }
                        },
                        overflow = TextOverflow.Ellipsis,
                        softWrap = false,
                        maxLines = 1
                    )
                },
                actions = {
                    IconButton(onClick = {
                        jumpToToday()
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.calendar),
                            contentDescription = "Gehe zu heute"
                        )
                    }
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
            Row(
                modifier = Modifier.padding(top = 16.dp, start = 16.dp, end = 16.dp, bottom = 0.dp),
            ) {
                if (visibleMonth != null) {
                    Panda(
                        modifier = Modifier.align(Alignment.CenterVertically),
                        date = visibleMonth!!
                    )
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = agendaState
                ) {
                    items(days) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            val events =
                                events.filter { ti -> ti.endDate >= it && ti.startDate <= it }
                            Column {
                                Text(it.format(LocalDate.Format {
                                    dayOfWeek(
                                        DayOfWeekNames(
                                            "Mo.",
                                            "Di.",
                                            "Mi.",
                                            "Do.",
                                            "Fr.",
                                            "Sa.",
                                            "So."
                                        )
                                    )
                                    chars(", ")
                                    day()
                                    chars(" ")
                                    monthName(germanMonthNames)
                                }), style = MaterialTheme.typography.labelSmall)
                                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                    for (event in events) {
                                        Box(
                                            modifier = Modifier
                                                .background(
                                                    Color(event.color.toColorInt()),
                                                    shape = CircleShape.copy(CornerSize(8.dp))
                                                )
                                                .fillMaxSize()
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .padding(8.dp)
                                                    .fillMaxWidth(),
                                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                            ) {
                                                Text(
                                                    event.title,
                                                    style = MaterialTheme.typography.bodyMedium.copy(
                                                        color = colorResource(
                                                            colorYiqRes(
                                                                event.color
                                                            )
                                                        )
                                                    )
                                                )
                                                Text(
                                                    event.description,
                                                    style = MaterialTheme.typography.bodySmall.copy(
                                                        color = colorResource(
                                                            colorYiqRes(
                                                                event.color
                                                            )
                                                        )
                                                    )
                                                )
                                            }
                                            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                                                var showMore by remember { mutableStateOf(false) }
                                                var showDelete by remember { mutableStateOf(false) }
                                                var showEdit by remember { mutableStateOf(false) }

                                                IconButton(
                                                    onClick = { showMore = true },
                                                ) {
                                                    Icon(
                                                        Icons.Default.MoreVert,
                                                        contentDescription = "Mehr",
                                                        tint = colorResource(
                                                            colorYiqRes(
                                                                event.color
                                                            )
                                                        )
                                                    )
                                                }
                                                DropdownMenu(
                                                    expanded = showMore,
                                                    onDismissRequest = { showMore = false }
                                                ) {
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                "Event bearbeiten",
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis
                                                            )
                                                        },
                                                        onClick = {
                                                            showMore = false
                                                            showEdit = true
                                                        }
                                                    )
                                                    DropdownMenuItem(
                                                        text = {
                                                            Text(
                                                                "Event löschen",
                                                                maxLines = 1,
                                                                overflow = TextOverflow.Ellipsis,
                                                            )
                                                        },
                                                        onClick = {
                                                            showMore = false
                                                            showDelete = true
                                                        }
                                                    )
                                                }
                                                if (showDelete) {
                                                    AlertDialog(
                                                        onDismissRequest = { showDelete = false },
                                                        confirmButton = {
                                                            TextButton(
                                                                onClick = {
                                                                    deleteEvent(event)
                                                                    showDelete = false
                                                                },
                                                                colors = ButtonDefaults.textButtonColors(
                                                                    contentColor = MaterialTheme.colorScheme.error,
                                                                )
                                                            ) {
                                                                Text("Event löschen")
                                                            }
                                                        },
                                                        dismissButton = {
                                                            TextButton(
                                                                onClick = {
                                                                    showDelete = false
                                                                },
                                                                colors = ButtonDefaults.textButtonColors(
                                                                    contentColor = MaterialTheme.colorScheme.error,
                                                                )
                                                            ) {
                                                                Text("Event behalten")
                                                            }
                                                        },
                                                        title = { Text("Soll das Event gelöscht werden?") },
                                                        text = { Text("Möchtest du das Event \"${event.title}\" wirklich löschen?") },
                                                        containerColor = MaterialTheme.colorScheme.errorContainer,
                                                        textContentColor = MaterialTheme.colorScheme.onErrorContainer,
                                                        titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                                                    )
                                                }
                                                if (showEdit) {
                                                    EditEventDialog(
                                                        event,
                                                        { showEdit = false }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                                HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                            }
                        }
                    }
                }
            }
            if (fabClicked) {
                AddEventDialog(onFabFinished)
            }
        }
    }
}