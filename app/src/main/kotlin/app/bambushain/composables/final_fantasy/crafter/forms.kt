package app.bambushain.composables.final_fantasy.crafter

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
import app.bambushain.api.CraftersApi
import app.bambushain.model.Crafter
import app.bambushain.model.CrafterJob
import app.bambushain.model.toCrafterJob
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private class FormState(
    job: CrafterJob,
    level: String?,
) {
    var level by mutableStateOf(level ?: "")
    var job by mutableStateOf(job.getDisplayName())
}

private fun rememberFormState(
    crafter: Crafter? = null,
    defaultJob: CrafterJob = CrafterJob.Leatherworker
): FormState {
    return FormState(
        crafter?.job ?: defaultJob,
        crafter?.level,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CrafterFormSheet(
    button: @Composable () -> Unit,
    onDismiss: () -> Unit,
    state: FormState,
    availableJobs: List<CrafterJob> = CrafterJob.entries,
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
                            bitmap = ImageBitmap.imageResource(state.job.toCrafterJob().getIcon()),
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
fun CreateCrafter(
    onCreated: () -> Unit,
    onDismiss: () -> Unit,
    crafters: List<Crafter>,
    characterId: Int,
    craftersApi: CraftersApi = koinInject()
) {
    val availableJobs = remember {
        CrafterJob.entries.filter {
            crafters.none { f -> f.job == it }
        }
    }

    val formState = rememberFormState(defaultJob = availableJobs.first())

    val coroutineScope = rememberCoroutineScope()

    val createCrafter = {
        coroutineScope.launch {
            craftersApi.createCrafter(
                characterId,
                Crafter(
                    job = formState.job.toCrafterJob(),
                    level = formState.level,
                )
            )
            onCreated()
        }
    }

    CrafterFormSheet(
        button = {
            Button(
                onClick = {
                    createCrafter()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Handwerker erstellen")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        availableJobs = availableJobs,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditCrafter(
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    crafter: Crafter,
    characterId: Int,
    craftersApi: CraftersApi = koinInject()
) {
    val formState = rememberFormState(crafter = crafter)

    val coroutineScope = rememberCoroutineScope()

    val createCrafter = {
        coroutineScope.launch {
            craftersApi.updateCrafter(
                characterId,
                crafter.id,
                Crafter(
                    job = formState.job.toCrafterJob(),
                    level = formState.level,
                )
            )
            onSave()
        }
    }

    CrafterFormSheet(
        button = {
            Button(
                onClick = {
                    createCrafter()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Handwerker speichern")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        isEdit = true
    )
}