package app.bambushain.composables.final_fantasy.housing

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.bambushain.api.HousingApi
import app.bambushain.model.CharacterHousing
import app.bambushain.model.HousingDistrict
import app.bambushain.model.HousingType
import app.bambushain.model.toHousingDistrict
import app.bambushain.model.toHousingType
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHousing(
    onCreated: () -> Unit,
    onDismiss: () -> Unit,
    housings: List<CharacterHousing>,
    characterId: Int,
    housingApi: HousingApi = koinInject()
) {
    val privateAvailable by remember {
        derivedStateOf {
            housings.none { it.housingType == HousingType.Private }
        }
    }

    var selectedDistrict by remember { mutableStateOf(HousingDistrict.TheLavenderBeds.getDisplayName()) }
    var selectedType by remember { mutableStateOf((if (privateAvailable) HousingType.Private else HousingType.SharedApartment).getDisplayName()) }
    var selectedWard by remember { mutableStateOf("1") }
    var selectedPlot by remember { mutableStateOf("1") }

    var availableWards by remember { mutableStateOf((1..60).toList()) }
    var availablePlots by remember { mutableStateOf((1..60).toList()) }

    val coroutineScope = rememberCoroutineScope()

    val createHousing = {
        coroutineScope.launch {
            housingApi.createCharacterHousing(
                characterId,
                CharacterHousing(
                    selectedDistrict.toHousingDistrict(),
                    selectedType.toHousingType(),
                    selectedWard.toInt(),
                    selectedPlot.toInt()
                )
            )
            onCreated()
        }
    }

    LaunchedEffect(selectedWard, selectedDistrict) {
        val existingHousings =
            housings.filter { it.district.getDisplayName() == selectedDistrict && it.ward == selectedWard.toInt() }
        availablePlots = if (existingHousings.isNotEmpty()) {
            (1..60).filter { existingHousings.none { h -> h.plot == it } }
        } else {
            (1..60).toList()
        }
        selectedPlot = availablePlots.first().toString()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            if (privateAvailable) {
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
                        value = selectedType,
                        onValueChange = { selectedType = it },
                        label = { Text("Typ") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        interactionSource = interactionSource,
                    )
                    DropdownMenu(
                        expanded = dropdownOpen,
                        onDismissRequest = { dropdownOpen = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(HousingType.Private.getDisplayName()) },
                            onClick = {
                                selectedType = HousingType.Private.getDisplayName()
                                dropdownOpen = false
                            },
                        )
                        DropdownMenuItem(
                            text = { Text(HousingType.SharedApartment.getDisplayName()) },
                            onClick = {
                                selectedType = HousingType.SharedApartment.getDisplayName()
                                dropdownOpen = false
                            },
                        )
                    }
                }
            } else {
                TextField(
                    value = HousingType.SharedApartment.getDisplayName(),
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
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
                    value = selectedDistrict,
                    onValueChange = { selectedDistrict = it },
                    label = { Text("Gebiet") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                )
                DropdownMenu(
                    expanded = dropdownOpen,
                    onDismissRequest = { dropdownOpen = false }
                ) {
                    for (district in HousingDistrict.entries) {
                        DropdownMenuItem(
                            text = { Text(district.getDisplayName()) },
                            onClick = {
                                selectedDistrict = district.getDisplayName()
                                dropdownOpen = false
                            },
                        )
                    }
                }
            }
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
                    value = selectedWard,
                    onValueChange = { selectedWard = it },
                    label = { Text("Bezirk") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                )
                DropdownMenu(
                    expanded = dropdownOpen,
                    onDismissRequest = { dropdownOpen = false }
                ) {
                    for (ward in availableWards) {
                        DropdownMenuItem(
                            text = { Text(ward.toString()) },
                            onClick = {
                                selectedWard = ward.toString()
                                dropdownOpen = false
                            },
                        )
                    }
                }
            }
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
                    value = selectedPlot,
                    onValueChange = { selectedPlot = it },
                    label = { Text("Nummer") },
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    interactionSource = interactionSource,
                )
                DropdownMenu(
                    expanded = dropdownOpen,
                    onDismissRequest = { dropdownOpen = false }
                ) {
                    for (plot in availablePlots) {
                        DropdownMenuItem(
                            text = { Text(plot.toString()) },
                            onClick = {
                                selectedPlot = plot.toString()
                                dropdownOpen = false
                            },
                        )
                    }
                }
            }
            Button(
                onClick = {
                    createHousing()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Unterkunft erstellen")
            }
        }
    }
}