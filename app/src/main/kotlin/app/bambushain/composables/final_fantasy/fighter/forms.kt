package app.bambushain.composables.final_fantasy.fighter

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
import app.bambushain.api.FightersApi
import app.bambushain.model.Fighter
import app.bambushain.model.FighterJob
import app.bambushain.model.toFighterJob
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private class FormState(
    job: FighterJob,
    level: String?,
    gearScore: String?
) {
    var level by mutableStateOf(level ?: "")
    var gearScore by mutableStateOf(gearScore ?: "")
    var job by mutableStateOf(job.getDisplayName())
}

private fun rememberFormState(
    fighter: Fighter? = null,
    defaultJob: FighterJob = FighterJob.BlueMage
): FormState {
    return FormState(
        fighter?.job ?: defaultJob,
        fighter?.level,
        fighter?.gearScore
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FighterFormSheet(
    button: @Composable () -> Unit,
    onDismiss: () -> Unit,
    state: FormState,
    availableJobs: List<FighterJob> = FighterJob.entries,
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
                            bitmap = ImageBitmap.imageResource(state.job.toFighterJob().getIcon()),
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
            TextField(
                value = state.gearScore,
                onValueChange = { state.gearScore = it },
                label = { Text("Gear Score") },
                modifier = Modifier.fillMaxWidth(),
            )
            button()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateFighter(
    onCreated: () -> Unit,
    onDismiss: () -> Unit,
    fighters: List<Fighter>,
    characterId: Int,
    fightersApi: FightersApi = koinInject()
) {
    val availableJobs = remember {
        FighterJob.entries.filter {
            fighters.none { f -> f.job == it }
        }
    }

    val formState = rememberFormState(defaultJob = availableJobs.first())

    val coroutineScope = rememberCoroutineScope()

    val createFighter = {
        coroutineScope.launch {
            fightersApi.createFighter(
                characterId,
                Fighter(
                    job = formState.job.toFighterJob(),
                    level = formState.level,
                    gearScore = formState.gearScore
                )
            )
            onCreated()
        }
    }

    FighterFormSheet(
        button = {
            Button(
                onClick = {
                    createFighter()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kämpfer erstellen")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        availableJobs = availableJobs,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFighter(
    onSave: () -> Unit,
    onDismiss: () -> Unit,
    fighter: Fighter,
    characterId: Int,
    fightersApi: FightersApi = koinInject()
) {
    val formState = rememberFormState(fighter = fighter)

    val coroutineScope = rememberCoroutineScope()

    val createFighter = {
        coroutineScope.launch {
            fightersApi.updateFighter(
                characterId,
                fighter.id,
                Fighter(
                    job = formState.job.toFighterJob(),
                    level = formState.level,
                    gearScore = formState.gearScore
                )
            )
            onSave()
        }
    }

    FighterFormSheet(
        button = {
            Button(
                onClick = {
                    createFighter()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Kämpfer speichern")
            }
        },
        onDismiss = onDismiss,
        state = formState,
        isEdit = true
    )
}