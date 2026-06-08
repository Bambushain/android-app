package app.bambushain.composables

import android.util.Log
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridFlow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.DateRangePickerState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.setSelectedDate
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
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.toColorInt
import app.bambushain.R
import app.bambushain.api.CalendarApi
import app.bambushain.api.GrovesApi
import app.bambushain.getBambooToken
import app.bambushain.model.EventReminder
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
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.coroutines.cancellation.CancellationException
import kotlin.time.Clock
import kotlin.time.Duration.Companion.seconds
import kotlin.time.Instant
import kotlin.time.toJavaInstant
import app.bambushain.api.R as apiR

val germanMonthNames = MonthNames(
    "Januar",
    "Februar",
    "März",
    "April",
    "Mai",
    "Juni",
    "Juli",
    "August",
    "September",
    "Oktober",
    "November",
    "Dezember"
)

@ColorRes
fun colorYiqRes(data: String): Int {
    val color = Color(data.toColorInt())
    val yiq =
        ((color.red * 255 * 299) + (color.green * 255 * 587) + (color.blue * 255 * 114)) / 1000
    return if (yiq >= 128) {
        R.color.color_yiq_dark
    } else {
        R.color.color_yiq_light
    }
}

@OptIn(ExperimentalGridApi::class)
@Composable
fun ColorPickerDialog(
    color: String? = null,
    colorPicked: (color: String) -> Unit,
    onDismissRequest: () -> Unit
) {
    val colors = stringArrayResource(R.array.colors)
    var selectedColor by remember { mutableStateOf(color ?: colors.first()) }

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = { colorPicked(selectedColor) }) {
                Text("Farbe wählen")
            }
        },
        title = {
            Text("Wähle eine Farbe")
        },
        text = {
            Grid({
                gap(8.dp)
                flow = GridFlow.Row
                repeat(4) {
                    column(64.dp)
                    row(64.dp)
                }
            }) {
                colors.forEach {
                    IconButton(
                        onClick = {
                            selectedColor = it
                        },
                        modifier = Modifier
                            .width(64.dp)
                            .height(64.dp),
                        shape = CircleShape.copy(CornerSize(32.dp)),
                        colors = IconButtonDefaults.iconButtonColors()
                            .copy(
                                containerColor = Color(it.toColorInt()),
                                contentColor = Color(it.toColorInt())
                            )
                    ) {
                        if (selectedColor == it) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.check),
                                contentDescription = "Ausgewählte Farbe",
                                tint = colorResource(colorYiqRes(it))
                            )
                        }
                    }
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
class EventFormState constructor(
    val titleState: TextFieldState,
    val descriptionState: TextFieldState,
    val dateRangeState: DateRangePickerState,
    val startTimeState: TimePickerState,
    val endTimeState: TimePickerState,
    hasTime: Boolean = true,
    isPrivate: Boolean = true,
    color: String,
    reminders: List<LocalDateTime> = emptyList(),
    selectedGrove: Grove? = null,
) {
    var hasTime by mutableStateOf(hasTime)
    var isPrivate by mutableStateOf(isPrivate)
    var color by mutableStateOf(color)
    var reminders by mutableStateOf(reminders)
    var selectedGrove by mutableStateOf(selectedGrove)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun rememberEventFormState(
    event: GroveEvent? = null
): EventFormState {
    val titleState = rememberTextFieldState(event?.title ?: "")
    val descriptionState = rememberTextFieldState(event?.description ?: "")
    val dateRangeState = rememberDateRangePickerState(
        event?.startDate?.toJavaLocalDate(),
        event?.endDate?.toJavaLocalDate()
    )
    val startTimeState = rememberTimePickerState(
        event?.startTime?.hour ?: 0,
        event?.startTime?.minute ?: 0
    )
    val endTimeState = rememberTimePickerState(
        event?.endTime?.hour ?: 0,
        event?.endTime?.minute ?: 0
    )
    val colors = stringArrayResource(R.array.colors)

    return remember {
        EventFormState(
            titleState = titleState,
            descriptionState = descriptionState,
            dateRangeState = dateRangeState,
            startTimeState = startTimeState,
            endTimeState = endTimeState,
            hasTime = event?.startTime != null,
            isPrivate = event?.isPrivate ?: true,
            color = event?.color ?: colors.random(),
            reminders = event?.reminder?.map {
                it.`when`.toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ).toJavaLocalDateTime()
            } ?: emptyList(),
            selectedGrove = event?.grove,
        )
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun EventForm(
    state: EventFormState,
    groveEnabled: Boolean = true,
    grovesApi: GrovesApi = koinInject(),
) {
    var datePickerOpen by remember { mutableStateOf(false) }
    var colorPickerOpen by remember { mutableStateOf(false) }

    var groves by remember { mutableStateOf(emptyList<Grove>()) }

    val focusManager = LocalFocusManager.current

    val dateRangeInteractionSource = remember { MutableInteractionSource() }
    val dateRangeInteractions by dateRangeInteractionSource.collectIsPressedAsState()

    val titleIsError by remember {
        derivedStateOf {
            state.titleState.text.isEmpty() && state.titleState.undoState.canUndo
        }
    }

    LaunchedEffect(Unit) {
        groves = grovesApi.getGroves().body()!!
    }

    LaunchedEffect(dateRangeInteractions) {
        if (dateRangeInteractions) datePickerOpen = true
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .widthIn(max = 512.dp)
    ) {
        item {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.titleState,
                lineLimits = TextFieldLineLimits.SingleLine,
                keyboardOptions = KeyboardOptions.Default.copy(KeyboardCapitalization.Sentences),
                isError = titleIsError,
                label = { Text("Titel") },
                supportingText = {
                    if (titleIsError) {
                        Text("Der Titel ist erforderlich")
                    }
                }
            )
        }
        item {
            TextField(
                modifier = Modifier.fillMaxWidth(),
                state = state.descriptionState,
                label = { Text("Beschreibung (optional)") },
            )
        }
        item {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(modifier = Modifier.weight(1f)) {
                    TextField(
                        value = state.dateRangeState.getSelectedStartDate()
                            ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                            ?: "Von",
                        onValueChange = {},
                        label = { Text("Von") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = dateRangeInteractionSource,
                    )
                }
                Box(modifier = Modifier.weight(1f)) {
                    TextField(
                        value = state.dateRangeState.getSelectedEndDate()
                            ?.format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM))
                            ?: "Bis",
                        onValueChange = {},
                        label = { Text("Bis") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = dateRangeInteractionSource,
                    )
                }
            }
        }
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(
                    checked = state.hasTime,
                    onCheckedChange = {
                        state.hasTime = it
                    }
                )
                Text("Hat Zeiten", style = MaterialTheme.typography.labelLarge)
            }
        }
        if (state.hasTime) {
            item {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        var pickerOpen by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        val interactions by interactionSource.collectIsPressedAsState()

                        LaunchedEffect(interactions) {
                            if (interactions) {
                                pickerOpen = true
                            }
                        }
                        TextField(
                            value = "${
                                state.startTimeState.hour.toString().padStart(2, '0')
                            }:${state.startTimeState.minute.toString().padStart(2, '0')}",
                            onValueChange = {},
                            label = { Text("Startzeit") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            interactionSource = interactionSource,
                        )
                        if (pickerOpen) {
                            TimePickerDialog(
                                onDismissRequest = { pickerOpen = false },
                                confirmButton = {
                                    TextButton(onClick = { pickerOpen = false }) {
                                        Text("Startzeit übernehmen")
                                    }
                                },
                                title = { Text("Startzeit wählen") },
                            ) {
                                TimePicker(state = state.startTimeState)
                            }
                        }
                    }
                    Box(modifier = Modifier.weight(1f)) {
                        var pickerOpen by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        val interactions by interactionSource.collectIsPressedAsState()

                        LaunchedEffect(interactions) {
                            if (interactions) {
                                pickerOpen = true
                            }
                        }
                        TextField(
                            value = "${
                                state.endTimeState.hour.toString().padStart(2, '0')
                            }:${state.endTimeState.minute.toString().padStart(2, '0')}",
                            onValueChange = {},
                            label = { Text("Endzeit") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            interactionSource = interactionSource,
                        )
                        if (pickerOpen) {
                            TimePickerDialog(
                                onDismissRequest = { pickerOpen = false },
                                confirmButton = {
                                    TextButton(onClick = { pickerOpen = false }) {
                                        Text("Endzeit übernehmen")
                                    }
                                },
                                title = { Text("Endzeit wählen") },
                            ) {
                                TimePicker(state = state.endTimeState)
                            }
                        }
                    }
                }
            }
        }
        item {
            Button(
                onClick = { colorPickerOpen = true },
                colors = ButtonDefaults.buttonColors()
                    .copy(containerColor = Color(state.color.toColorInt()))
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Image(
                        imageVector = ImageVector.vectorResource(R.drawable.eyedropper),
                        contentDescription = "Wähle eine Farbe",
                        colorFilter = ColorFilter.tint(colorResource(colorYiqRes(state.color)))
                    )
                }
            }
        }
        if (groveEnabled) {
            if (groves.isNotEmpty()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = state.isPrivate,
                            onCheckedChange = {
                                state.isPrivate = it
                                state.selectedGrove = if (!state.isPrivate && groves.size == 1) {
                                    groves.first()
                                } else {
                                    null
                                }
                            }
                        )
                        Text("Nur für mich", style = MaterialTheme.typography.labelLarge)
                    }
                }
            }
            if (!state.isPrivate && groves.isNotEmpty()) {
                item {
                    Box {
                        var dropdownOpen by remember { mutableStateOf(false) }
                        val interactionSource = remember { MutableInteractionSource() }
                        val interactions by interactionSource.collectIsPressedAsState()

                        LaunchedEffect(interactions) {
                            if (interactions) {
                                dropdownOpen = true
                            }
                        }

                        TextField(
                            value = state.selectedGrove?.name ?: "",
                            onValueChange = {},
                            label = { Text("Hain") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            interactionSource = interactionSource,
                        )
                        if (groves.size > 1) {
                            DropdownMenu(
                                expanded = dropdownOpen,
                                onDismissRequest = { dropdownOpen = false }
                            ) {
                                for (grove in groves) {
                                    DropdownMenuItem(
                                        text = { Text(grove.name) },
                                        onClick = {
                                            state.selectedGrove = grove
                                            dropdownOpen = false
                                        },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
        item {
            Text("Erinnerungen", style = MaterialTheme.typography.titleLarge)
        }
        item {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                val reminderDateState = rememberDatePickerState()
                val reminderTimeState = rememberTimePickerState()

                var datePickerOpen by remember { mutableStateOf(false) }
                var timePickerOpen by remember { mutableStateOf(false) }

                val interactionSource = remember { MutableInteractionSource() }
                val interactions by interactionSource.collectIsPressedAsState()

                LaunchedEffect(interactions) {
                    if (interactions) {
                        datePickerOpen = true
                    }
                }
                TextField(
                    value = reminderDateState.getSelectedDate()
                        ?.atTime(reminderTimeState.hour, reminderTimeState.minute)
                        ?.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM))
                        ?: "",
                    onValueChange = {},
                    label = { Text("Erinnerungszeit") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(1f),
                    interactionSource = interactionSource,
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                focusManager.clearFocus(true)
                                if (reminderDateState.selectedDateMillis != null) {
                                    state.reminders += reminderDateState.getSelectedDate()!!
                                        .atTime(
                                            reminderTimeState.hour,
                                            reminderTimeState.minute
                                        )
                                    reminderDateState.setSelectedDate(null)
                                    reminderTimeState.hour = 0
                                    reminderTimeState.minute = 0
                                }
                            },
                            enabled = reminderDateState.selectedDateMillis != null,
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(R.drawable.bell_plus),
                                contentDescription = "Erinnerung festlegen"
                            )
                        }
                    }
                )
                if (datePickerOpen) {
                    DatePickerDialog(
                        onDismissRequest = { datePickerOpen = false },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    datePickerOpen = false
                                    timePickerOpen = true
                                }
                            ) {
                                Text("Tag übernehmen")
                            }
                        },
                    ) {
                        DatePicker(reminderDateState)
                    }
                }
                if (timePickerOpen) {
                    TimePickerDialog(
                        onDismissRequest = { timePickerOpen = false },
                        confirmButton = {
                            TextButton(onClick = { timePickerOpen = false }) {
                                Text("Zeit übernehmen")
                            }
                        },
                        title = { Text("Zeit wählen") },
                    ) {
                        TimePicker(state = reminderTimeState)
                    }
                }
            }
        }
        items(state.reminders) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    it.format(
                        DateTimeFormatter.ofLocalizedDateTime(
                            FormatStyle.MEDIUM
                        )
                    ),
                )
                IconButton(onClick = {
                    state.reminders -= it
                }) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.bell_minus),
                        contentDescription = "Erinnerung entfernen"
                    )
                }
            }
            if (state.reminders.indexOf(it) != state.reminders.size - 1) {
                HorizontalDivider(modifier = Modifier.fillMaxWidth())
            }
        }
    }
    if (datePickerOpen) {
        AlertDialog(
            onDismissRequest = { datePickerOpen = false },
            confirmButton = {
                TextButton(onClick = { datePickerOpen = false }) {
                    Text("Fertig")
                }
            },
            title = {
                Text("Wähle den Zeitraum")
            },
            text = {
                DateRangePicker(
                    state = state.dateRangeState,
                    title = null
                )
            }
        )
    }
    if (colorPickerOpen) {
        ColorPickerDialog(
            color = state.color,
            colorPicked = {
                state.color = it
                colorPickerOpen = false
            },
            onDismissRequest = { colorPickerOpen = false })
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AddEventDialog(
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
fun EditEventDialog(
    event: GroveEvent,
    onDismissRequest: () -> Unit,
    calendarApi: CalendarApi = koinInject(),
) {
    val eventId = remember { event.id!! }

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
    var eventToDelete by remember { mutableStateOf<GroveEvent?>(null) }
    var eventToEdit by remember { mutableStateOf<GroveEvent?>(null) }
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

    val deleteEvent = {
        coroutineScope.launch {
            if (eventToDelete != null) {
                val response = calendarApi.deleteGroveEvent(eventToDelete!!.id!!)
                if (response.isSuccessful) {
                    eventToDelete = null
                } else {
                    snackbarHostState.showSnackbar("Das Event konnte leider nicht gelöscht werden")
                }
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
                modifier = Modifier.padding(16.dp),
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
                                                var showMore by remember {
                                                    mutableStateOf(
                                                        false
                                                    )
                                                }
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
                                                            eventToEdit = event
                                                            showMore = false
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
                                                            eventToDelete = event
                                                            showMore = false
                                                        }
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
            eventToDelete?.let {
                AlertDialog(
                    onDismissRequest = { eventToDelete = null },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                deleteEvent()
                            },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = MaterialTheme.colorScheme.error,
                            )
                        ) {
                            Text("Event löschen")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = {
                            eventToDelete = null
                        }) {
                            Text("Event behalten")
                        }
                    },
                    title = { Text("Soll das Event gelöscht werden?") },
                    text = { Text("Möchtest du das Event \"${it.title}\" wirklich löschen?") },
                    containerColor = MaterialTheme.colorScheme.errorContainer,
                    textContentColor = MaterialTheme.colorScheme.onErrorContainer,
                    titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                )
            }
            eventToEdit?.let { EditEventDialog(it, { eventToEdit = null }) }
            if (fabClicked) {
                AddEventDialog(onFabFinished)
            }
        }
    }
}