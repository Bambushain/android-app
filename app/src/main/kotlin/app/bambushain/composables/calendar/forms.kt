package app.bambushain.composables.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalGridApi
import androidx.compose.foundation.layout.Grid
import androidx.compose.foundation.layout.GridFlow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.TimePickerState
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.getSelectedEndDate
import androidx.compose.material3.getSelectedStartDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.material3.setSelectedDate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.core.graphics.toColorInt
import app.bambushain.R
import app.bambushain.api.GrovesApi
import app.bambushain.model.Grove
import app.bambushain.model.GroveEvent
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.koin.compose.koinInject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalGridApi::class)
@Composable
internal fun ColorPickerDialog(
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
internal class EventFormState(
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
internal fun rememberEventFormState(
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
internal fun EventForm(
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
