package app.bambushain.composables.final_fantasy.characters

import android.widget.Toast
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import app.bambushain.api.CharactersApi
import app.bambushain.api.CustomFieldsApi
import app.bambushain.api.FreeCompaniesApi
import app.bambushain.model.Character
import app.bambushain.model.CharacterCustomField
import app.bambushain.model.CharacterRace
import app.bambushain.model.CustomField
import app.bambushain.model.FreeCompany
import app.bambushain.model.toCharacterRace
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private class FormState(
    availableCustomFields: List<CustomField>,
    character: Character? = null,
) {
    var race by mutableStateOf(
        character?.race?.getDisplayName() ?: CharacterRace.Lalafell.getDisplayName()
    )
    var name by mutableStateOf(character?.name ?: "")
    var world by mutableStateOf(character?.world ?: "Adamantoise")
    var datacenter by mutableStateOf(character?.datacenter ?: "Aether")
    var customFields by mutableStateOf(availableCustomFields.associate {
        it.label to (character?.customFields?.find { c -> c.label == it.label }?.values?.toList()
            ?: listOf())
    }.toMap())
    var freeCompany by mutableStateOf(character?.freeCompany?.name ?: "")
}

@Composable
private fun rememberFormState(
    availableCustomFields: List<CustomField>,
    character: Character? = null,
): FormState {
    return FormState(availableCustomFields, character)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharacterFormSheet(
    onDismiss: () -> Unit,
    availableCustomFields: List<CustomField>,
    state: FormState,
    freeCompanies: List<FreeCompany>,
    button: @Composable (state: FormState) -> Unit,
) {
    val datacenters = mapOf(
        "Aether" to listOf(
            "Adamantoise",
            "Cactuar",
            "Faerie",
            "Gilgamesh",
            "Jenova",
            "Midgardsormr",
            "Sargatanas",
            "Siren",
        ),
        "Chaos" to listOf(
            "Cerberus",
            "Louisoix",
            "Moogle",
            "Omega",
            "Phantom",
            "Ragnarok",
            "Sagittarius",
            "Spriggan"
        ),
        "Crystal" to listOf(
            "Balmung",
            "Brynhildr",
            "Coerul",
            "Diabolos",
            "Goblin",
            "Malboro",
            "Mateur",
            "Zalera",
        ),
        "Dynamis" to listOf(
            "Cuchulainn",
            "Golem",
            "Halicarnassus",
            "Kraken",
            "Maduin",
            "Marilith",
            "Rafflesia",
            "Seraph",
        ),
        "Elemental" to listOf(
            "Aegis",
            "Atomos",
            "Carbuncle",
            "Garuda",
            "Gungnir",
            "Kujata",
            "Tonberry",
            "Typhon",
        ),
        "Gaia" to listOf(
            "Alexander",
            "Bahamut",
            "Durandal",
            "Fenrir",
            "Ifrit",
            "Ridill",
            "Tiarmat",
            "Ultima",
        ),
        "Light" to listOf(
            "Alpha",
            "Lich",
            "Odin",
            "Phoenix",
            "Raiden",
            "Shiva",
            "Twintania"
        ),
        "Mana" to listOf(
            "Anima",
            "Asura",
            "Chocobo",
            "Hades",
            "Ixion",
            "Masamune",
            "Pandaemonium",
            "Titan",
        ),
        "Materia" to listOf(
            "Bismarck",
            "Ravana",
            "Sephiroth",
            "Sophia",
            "Zurvan",
        ),
        "Meteor" to listOf(
            "Belias",
            "Mandragora",
            "Ramuh",
            "Shinryu",
            "Unicorn",
            "Valefor",
            "Yojimbo",
            "Zeromus",
        ),
        "Primal" to listOf(
            "Behemoth",
            "Excalibur",
            "Exodus",
            "Famfrit",
            "Hyperion",
            "Lamia",
            "Leviathan",
            "Ultros",
        ),
    )

    val focusManager = LocalFocusManager.current

    ModalBottomSheet(onDismissRequest = onDismiss) {
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            item {
                TextField(
                    value = state.name,
                    onValueChange = { state.name = it },
                    label = { Text("Name") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    keyboardActions = KeyboardActions({
                        focusManager.clearFocus(true)
                    }),
                    keyboardOptions = KeyboardOptions.Default.copy(capitalization = KeyboardCapitalization.Sentences)
                )
            }
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
                        value = state.race,
                        onValueChange = { state.race = it },
                        label = { Text("Rasse") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interactionSource,
                    )
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        for (race in CharacterRace.entries) {
                            DropdownMenuItem(
                                text = { Text(race.getDisplayName()) },
                                onClick = {
                                    state.race = race.getDisplayName()
                                    dropdownOpen = false
                                },
                            )
                        }
                    }
                }
            }
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
                        value = state.datacenter.ifEmpty {
                            "Ohne Datenzentrum"
                        },
                        onValueChange = { state.datacenter = it },
                        label = { Text("Datenzentrum") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interactionSource,
                    )
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        for (center in datacenters.keys) {
                            DropdownMenuItem(
                                text = { Text(center) },
                                onClick = {
                                    state.datacenter = center
                                    dropdownOpen = false
                                },
                            )
                        }
                    }
                }
            }
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
                        value = state.world.ifEmpty {
                            "Ohne Stammwelt"
                        },
                        onValueChange = { state.world = it },
                        label = { Text("Stammwelt") },
                        readOnly = true,
                        enabled = state.datacenter.isNotEmpty(),
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interactionSource,
                    )
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        for (world in datacenters[state.datacenter] ?: emptyList()) {
                            DropdownMenuItem(
                                text = { Text(world) },
                                onClick = {
                                    state.world = world
                                    dropdownOpen = false
                                },
                            )
                        }
                    }
                }
            }
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
                        value = state.freeCompany.ifEmpty {
                            "Keine freie Gesellschaft"
                        },
                        onValueChange = { state.freeCompany = it },
                        label = { Text("Freie Gesellschaft") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interactionSource,
                    )
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Keine freie Gesellschaft") },
                            onClick = {
                                state.freeCompany = ""
                                dropdownOpen = false
                            },
                        )
                        for (freeCompany in freeCompanies) {
                            DropdownMenuItem(
                                text = { Text(freeCompany.name) },
                                onClick = {
                                    state.freeCompany = freeCompany.name
                                    dropdownOpen = false
                                },
                            )
                        }
                    }
                }
            }
            items(availableCustomFields) {
                Column {
                    Text(it.label, style = MaterialTheme.typography.titleMedium)
                    for (option in it.options) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.customFields[it.label]?.contains(option.label)
                                    ?: false,
                                onCheckedChange = { new ->
                                    val customFields = state.customFields.toMutableMap()
                                    val newList = customFields[it.label]!!.toMutableList()
                                    if (new) {
                                        newList += option.label
                                    } else {
                                        newList -= option.label
                                    }
                                    customFields[it.label] = newList
                                    state.customFields = customFields
                                }
                            )
                            Text(option.label, style = MaterialTheme.typography.bodyLarge)
                        }
                    }
                }
            }
            item {
                button(state)
            }
        }
    }
}

@Composable
private fun CharacterFormSheetInner(
    onDismiss: () -> Unit,
    availableCustomFields: List<CustomField>,
    freeCompanies: List<FreeCompany>,
    button: @Composable (state: FormState) -> Unit,
    character: Character? = null
) {
    val state =
        rememberFormState(availableCustomFields, character)

    CharacterFormSheet(
        onDismiss = onDismiss,
        availableCustomFields = availableCustomFields,
        state = state,
        button = button,
        freeCompanies = freeCompanies,
    )
}

@Composable
fun CreateCharacter(
    onDismiss: () -> Unit,
    onCreated: () -> Unit,
    freeCompaniesApi: FreeCompaniesApi = koinInject(),
    customFieldsApi: CustomFieldsApi = koinInject(),
    charactersApi: CharactersApi = koinInject(),
) {
    var freeCompanies by remember { mutableStateOf(emptyList<FreeCompany>()) }
    var availableCustomFields by remember { mutableStateOf(emptyList<CustomField>()) }

    var loading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val createCharacter = { state: FormState ->
        coroutineScope.launch {
            val res = charactersApi.createCharacter(
                Character(
                    race = state.race.toCharacterRace(),
                    name = state.name,
                    world = state.world,
                    datacenter = state.datacenter,
                    customFields = state.customFields.map {
                        CharacterCustomField(
                            it.key, it.value.toList()
                        )
                    },
                    freeCompany = freeCompanies.find { it.name == state.freeCompany }
                )
            )
            if (!res.isSuccessful) {
                Toast.makeText(
                    context,
                    "Ein Charakter mit dem Namen ${state.name} existiert bereits",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                onCreated()
            }
        }
    }

    LaunchedEffect(Unit) {
        availableCustomFields = customFieldsApi.getCustomFields().body()!!
        freeCompanies = freeCompaniesApi.getFreeCompanies().body()!!
        loading = false
    }

    if (!loading) {
        CharacterFormSheetInner(
            onDismiss = onDismiss,
            availableCustomFields = availableCustomFields,
            freeCompanies = freeCompanies,
            button = { state ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        createCharacter(state)
                    }
                ) {
                    Text("Charakter erstellen")
                }
            }
        )
    }
}

@Composable
fun UpdateCharacter(
    onDismiss: () -> Unit,
    onUpdated: () -> Unit,
    character: Character,
    freeCompaniesApi: FreeCompaniesApi = koinInject(),
    customFieldsApi: CustomFieldsApi = koinInject(),
    charactersApi: CharactersApi = koinInject(),
) {
    var freeCompanies by remember { mutableStateOf(emptyList<FreeCompany>()) }
    var availableCustomFields by remember { mutableStateOf(emptyList<CustomField>()) }

    var loading by remember { mutableStateOf(true) }

    val coroutineScope = rememberCoroutineScope()

    val context = LocalContext.current

    val updateCharacter = { state: FormState ->
        coroutineScope.launch {
            val res = charactersApi.updateCharacter(
                character.id,
                Character(
                    race = state.race.toCharacterRace(),
                    name = state.name,
                    world = state.world,
                    datacenter = state.datacenter,
                    customFields = state.customFields.map {
                        CharacterCustomField(
                            it.key, it.value.toList()
                        )
                    },
                    freeCompany = freeCompanies.find { it.name == state.freeCompany }
                )
            )
            if (!res.isSuccessful) {
                Toast.makeText(
                    context,
                    "Ein Charakter mit dem Namen ${state.name} existiert bereits",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                onUpdated()
            }
        }
    }

    LaunchedEffect(Unit) {
        availableCustomFields = customFieldsApi.getCustomFields().body()!!
        freeCompanies = freeCompaniesApi.getFreeCompanies().body()!!
        loading = false
    }

    if (!loading) {
        CharacterFormSheetInner(
            onDismiss = onDismiss,
            availableCustomFields = availableCustomFields,
            freeCompanies = freeCompanies,
            character = character,
            button = { state ->
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        updateCharacter(state)
                    }
                ) {
                    Text("Charakter speichern")
                }
            }
        )
    }
}