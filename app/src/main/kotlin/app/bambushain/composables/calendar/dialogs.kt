package app.bambushain.composables.calendar

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import app.bambushain.R
import app.bambushain.api.CalendarApi
import app.bambushain.model.EventReminder
import app.bambushain.model.GroveEvent
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import org.koin.compose.koinInject
import java.time.ZoneId
import kotlin.time.Clock
import kotlin.time.Instant
import kotlin.time.toJavaInstant

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun AddEventDialog(
    onDismissRequest: () -> Unit,
    calendarApi: CalendarApi = koinInject(),
) {
    val formState = rememberEventFormState()

    val context = LocalContext.current

    val titleIsError by remember {
        derivedStateOf {
            formState.titleState.text.isEmpty() && formState.titleState.undoState.canUndo
        }
    }

    val canCreate by remember {
        derivedStateOf {
            !titleIsError && formState.dateRangeState.selectedStartDateMillis != null && formState.dateRangeState.selectedEndDateMillis != null
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val createEvent = {
        val startDate = formState.dateRangeState.getSelectedStartDate()
        val endDate = formState.dateRangeState.getSelectedEndDate()
        if (startDate != null && endDate != null) {
            coroutineScope.launch() {
                val reminders = formState.reminders.map {
                    EventReminder(
                        `when` = Instant.fromEpochSeconds(
                            it.toEpochSecond(
                                ZoneId
                                    .systemDefault()
                                    .rules
                                    .getOffset(
                                        Clock.System.now().toJavaInstant()
                                    )
                            )
                        )
                    )
                }
                val event = if (formState.hasTime) {
                    GroveEvent(
                        title = formState.titleState.text.toString(),
                        description = formState.descriptionState.text.toString(),
                        startDate = LocalDate.fromEpochDays(
                            startDate.toEpochDay()
                        ),
                        endDate = LocalDate.fromEpochDays(endDate.toEpochDay()),
                        startTime = LocalTime(
                            formState.startTimeState.hour,
                            formState.startTimeState.minute
                        ),
                        endTime = LocalTime(
                            formState.endTimeState.hour,
                            formState.endTimeState.minute
                        ),
                        color = formState.color,
                        isPrivate = formState.isPrivate,
                        grove = formState.selectedGrove,
                        reminder = reminders
                    )
                } else {
                    GroveEvent(
                        title = formState.titleState.text.toString(),
                        description = formState.descriptionState.text.toString(),
                        startDate = LocalDate.fromEpochDays(
                            startDate.toEpochDay()
                        ),
                        endDate = LocalDate.fromEpochDays(endDate.toEpochDay()),
                        color = formState.color,
                        isPrivate = formState.isPrivate,
                        grove = formState.selectedGrove,
                        reminder = reminders
                    )
                }
                val result = calendarApi.createGroveEvent(event)
                if (result.isSuccessful) {
                    onDismissRequest()
                } else {
                    Toast.makeText(
                        context,
                        "Das Event konnte leider nicht erstellt werden",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Neues Event") },
                    navigationIcon = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(Icons.Default.Close, contentDescription = "Schließen")
                        }
                    },
                    actions = {
                        IconButton(onClick = createEvent, enabled = canCreate) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.calendar_check),
                                contentDescription = "Erstellen"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                EventForm(formState)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun EditEventDialog(
    event: GroveEvent,
    onDismissRequest: () -> Unit,
    calendarApi: CalendarApi = koinInject(),
) {
    val eventId = remember { event.id }

    val formState = rememberEventFormState(event)

    val context = LocalContext.current

    val titleIsError by remember {
        derivedStateOf {
            formState.titleState.text.isEmpty() && formState.titleState.undoState.canUndo
        }
    }

    val canUpdate by remember {
        derivedStateOf {
            !titleIsError && formState.dateRangeState.selectedStartDateMillis != null && formState.dateRangeState.selectedEndDateMillis != null
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val updateEvent = {
        val startDate = formState.dateRangeState.getSelectedStartDate()
        val endDate = formState.dateRangeState.getSelectedEndDate()
        if (startDate != null && endDate != null) {
            coroutineScope.launch() {
                val reminders = formState.reminders.map {
                    EventReminder(
                        `when` = Instant.fromEpochSeconds(
                            it.toEpochSecond(
                                ZoneId
                                    .systemDefault()
                                    .rules
                                    .getOffset(
                                        Clock.System.now().toJavaInstant()
                                    )
                            )
                        )
                    )
                }
                val event = if (formState.hasTime) {
                    GroveEvent(
                        title = formState.titleState.text.toString(),
                        description = formState.descriptionState.text.toString(),
                        startDate = LocalDate.fromEpochDays(
                            startDate.toEpochDay()
                        ),
                        endDate = LocalDate.fromEpochDays(endDate.toEpochDay()),
                        startTime = LocalTime(
                            formState.startTimeState.hour,
                            formState.startTimeState.minute
                        ),
                        endTime = LocalTime(
                            formState.endTimeState.hour,
                            formState.endTimeState.minute
                        ),
                        color = formState.color,
                        isPrivate = formState.isPrivate,
                        grove = formState.selectedGrove,
                        reminder = reminders
                    )
                } else {
                    GroveEvent(
                        title = formState.titleState.text.toString(),
                        description = formState.descriptionState.text.toString(),
                        startDate = LocalDate.fromEpochDays(
                            startDate.toEpochDay()
                        ),
                        endDate = LocalDate.fromEpochDays(endDate.toEpochDay()),
                        color = formState.color,
                        isPrivate = formState.isPrivate,
                        grove = formState.selectedGrove,
                        reminder = reminders
                    )
                }
                val result = calendarApi.updateGroveEvent(eventId, event)
                if (result.isSuccessful) {
                    onDismissRequest()
                } else {
                    Toast.makeText(
                        context,
                        "Das Event konnte leider nicht gespeichert werden",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Event bearbeiten") },
                    navigationIcon = {
                        IconButton(onClick = onDismissRequest) {
                            Icon(Icons.Default.Close, contentDescription = "Schließen")
                        }
                    },
                    actions = {
                        IconButton(onClick = updateEvent, enabled = canUpdate) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.calendar_check),
                                contentDescription = "Speichern"
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                EventForm(formState, groveEnabled = false)
            }
        }
    }
}
