package app.bambushain.composables.final_fantasy.housing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import app.bambushain.R
import app.bambushain.api.HousingApi
import app.bambushain.model.CharacterHousing
import app.bambushain.model.FreeCompanyHousing
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun HousingTab(
    fabClicked: Boolean,
    onFabFinished: () -> Unit,
    housings: List<CharacterHousing>,
    onHousingChanged: () -> Unit,
    characterId: Int,
    freeCompanyHousing: FreeCompanyHousing? = null,
    housingApi: HousingApi = koinInject()
) {
    val coroutineScope = rememberCoroutineScope()

    val deleteHousing = { id: Int ->
        coroutineScope.launch {
            housingApi.deleteCharacterHousing(characterId, id)
            onHousingChanged()
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(320.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(housings) {
            ElevatedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                ) {
                    Text(
                        it.district.getDisplayName(),
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(it.housingType.getDisplayName())
                    Text("Bezirk ${it.ward}")
                    Text("Nr. ${it.plot}")
                }
                Row(
                    modifier = Modifier.align(Alignment.End)
                ) {
                    var showDelete by remember { mutableStateOf(false) }

                    IconButton(onClick = {
                        showDelete = true
                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.delete),
                            contentDescription = "Löschen",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    if (showDelete) {
                        AlertDialog(
                            onDismissRequest = { showDelete = false },
                            title = { Text("${it.housingType.getDisplayName()} löschen") },
                            text = { Text("Soll die ${it.housingType.getDisplayName()} im Gebiet ${it.district.getDisplayName()} im Bezirk ${it.ward} mit der Nummer ${it.plot} wirklich gelöscht werden?") },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        deleteHousing(it.id)
                                        showDelete = false
                                    },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error,
                                    )
                                ) {
                                    Text("Löschen")
                                }
                            },
                            dismissButton = {
                                TextButton(
                                    onClick = { showDelete = false },
                                    colors = ButtonDefaults.textButtonColors(
                                        contentColor = MaterialTheme.colorScheme.error,
                                    )
                                ) {
                                    Text("Nicht löschen")
                                }
                            },
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                            textContentColor = MaterialTheme.colorScheme.onErrorContainer,
                            titleContentColor = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }
        }
        if (freeCompanyHousing != null) {
            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                    ) {
                        Text(
                            freeCompanyHousing.district.getDisplayName(),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text("Unterkunft einer freien Gesellschaft")
                        Text("Bezirk ${freeCompanyHousing.ward}")
                        Text("Nr. ${freeCompanyHousing.plot}")
                    }
                }
            }
        }
        if (freeCompanyHousing == null && housings.isEmpty()) {
            item {
                Text("Keine Unterkünfte vorhanden", style = MaterialTheme.typography.titleLarge)
            }
        }
    }
    if (fabClicked) {
        CreateHousing(
            onCreated = {
                onFabFinished()
                onHousingChanged()
            }, onDismiss = onFabFinished, housings = housings, characterId = characterId
        )
    }
}