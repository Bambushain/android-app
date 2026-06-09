package app.bambushain.composables.final_fantasy.gatherer

import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.unit.dp
import app.bambushain.api.GatherersApi
import app.bambushain.model.Gatherer
import app.bambushain.model.GathererJob
import app.bambushain.model.toGathererJob
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private class FormState(
    job: GathererJob,
    level: String?,
) {
    var level by mutableStateOf(level ?: "")
    var job by mutableStateOf(job.getDisplayName())
}

private fun rememberFormState(
    gatherer: Gatherer? = null,
    defaultJob: GathererJob = GathererJob.Botanist
): FormState {
    return FormState(
        gatherer?.job ?: defaultJob,
        gatherer?.level,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GathererFormSheet(
    button: @Composable () -> Unit,
    onDismiss: () -> Unit,
    state: FormState,
    availableJobs: List<GathererJob> = GathererJob.entries,
    isEdit: Boolean = false
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box {
                var dropdownOpen by remember { mutableStateOf(false) }
                val interactionSource = remember { MutableInteractionSource() }
                val interactions by interactionSource.collectIsPressedAsState()

                LaunchedEffect(interactions) {
                    if (interactions && availableJobs.isNotEmpty() && !isEdit) {
                        dropdownOpen = true
                    }
                }

                TextField(
                    value = state.job,
                    leadingIcon = {
                        Image(
                            bitmap = ImageBitmap.imageResource(state.job.toGathererJob().getIcon()),
                            contentDescription = null
                        )
                    },
                    onValueChange = { state.job = it },
                    label = { Text("Job") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                )
                DropdownMenu(
                    expanded = dropdownOpen,
                    onDismissRequest = { dropdownOpen = false }
                ) {
                    for (job in availableJobs) {
                        DropdownMenuItem(
                            text = { Text(job.getDisplayName()) },
                            leadingIcon = {
                                Image(
                                    bitmap = ImageBitmap.imageResource(job.getIcon()),
                                    contentDescription = null
                                )
                            },
                            onClick = {
                                state.job = job.getDisplayName()
                                dropdownOpen = false
                            },
                        )
                    }
                }
            }
            TextField(
                value = state.level,
                onValueChange = { state.level = it },
                label = { Text("Level") },
                modifier = Modifier.fillMaxWidth(),
            )
            button()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateGatherer(
    onCreated: () -> Unit,
    onDismiss: () -> Unit,
    gatherers: List<Gatherer>,
    characterId: Int,
    gatherersApi: GatherersApi = koinInject()
) {
    val availableJobs = remember {
        GathererJob.entries.filter {
            gatherers.none { f -> f.job == it }
        }
    }

    val formState = rememberFormState(defaultJob = availableJobs.first())

    val coroutineScope = rememberCoroutineScope()

    val createGatherer = {
        coroutineScope.launch {
            gatherersApi.createGatherer(
                characterId,
                Gatherer(
                    job = formState.job.toGathererJob(),
                    level = formState.level,
                )
            )
            onCreated()
        }
    }

    GathererFormSheet(
        button = {
            Button(
                onClick = {
                    createGatherer()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sammler erstellen")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        availableJobs = availableJobs,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGatherer(
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    gatherer: Gatherer,
    characterId: Int,
    gatherersApi: GatherersApi = koinInject()
) {
    val formState = rememberFormState(gatherer = gatherer)

    val coroutineScope = rememberCoroutineScope()

    val createGatherer = {
        coroutineScope.launch {
            gatherersApi.updateGatherer(
                characterId,
                gatherer.id,
                Gatherer(
                    job = formState.job.toGathererJob(),
                    level = formState.level,
                )
            )
            onSave()
        }
    }

    GathererFormSheet(
        button = {
            Button(
                onClick = {
                    createGatherer()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Sammler speichern")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        isEdit = true
    )
}