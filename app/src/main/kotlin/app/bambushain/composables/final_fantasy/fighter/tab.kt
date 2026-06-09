package app.bambushain.composables.final_fantasy.fighter

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import app.bambushain.api.FightersApi
import app.bambushain.model.Fighter
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@Composable
fun FighterTab(
    actionClicked: Boolean,
    onActionFinished: () -> Unit,
    fighters: List<Fighter>,
    onFighterChanged: () -> Job,
    characterId: Int,
    fightersApi: FightersApi = koinInject()
) {
    val coroutineScope = rememberCoroutineScope()

    val deleteFighter = { id: Int ->
        coroutineScope.launch {
            fightersApi.deleteFighter(characterId, id)
            onFighterChanged()
        }
    }

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(320.dp),
        contentPadding = PaddingValues(16.dp),
        verticalItemSpacing = 16.dp,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(fighters) {
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Box {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Image(
                            bitmap = ImageBitmap.imageResource(it.job.getIcon()),
                            contentDescription = it.job.getDisplayName(),
                            modifier = Modifier.size(112.dp)
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(
                                it.job.getDisplayName(),
                                style = MaterialTheme.typography.headlineMedium
                            )
                            if (it.level.isNotEmpty()) {
                                Text("Level: ${it.level}")
                            } else {
                                Text("Kein Level angegeben")
                            }
                            if (it.gearScore.isNotEmpty()) {
                                Text("Gear Score: ${it.gearScore}")
                            } else {
                                Text("Kein Gear Score angegeben")
                            }
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.TopEnd)) {
                        var showEdit by remember { mutableStateOf(false) }
                        var showDelete by remember { mutableStateOf(false) }
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
                            )
                        }
                        DropdownMenu(
                            expanded = showMore,
                            onDismissRequest = { showMore = false }
                        ) {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Kämpfer bearbeiten",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                },
                                onClick = {
                                    showEdit = true
                                    showMore = false
                                }
                            )
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        "Kämpfer löschen",
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                },
                                onClick = {
                                    showDelete = true
                                    showMore = false
                                }
                            )
                        }
                        if (showEdit) {
                            EditFighter(
                                onSave = {
                                    onFighterChanged()
                                    showEdit = false
                                },
                                onDismiss = {
                                    showEdit = false
                                },
                                fighter = it,
                                characterId = characterId,
                            )
                        }
                        if (showDelete) {
                            AlertDialog(
                                onDismissRequest = { showDelete = false },
                                title = { Text("${it.job.getDisplayName()} löschen") },
                                text = { Text("Soll der Kämpfer mit dem Job ${it.job.getDisplayName()} wirklich gelöscht werden?") },
                                confirmButton = {
                                    TextButton(onClick = {
                                        deleteFighter(it.id)
                                        showDelete = false
                                    }) {
                                        Text("Löschen")
                                    }
                                },
                                dismissButton = {
                                    TextButton(onClick = { showDelete = false }) {
                                        Text("Nicht löschen")
                                    }
                                },
                                containerColor = MaterialTheme.colorScheme.errorContainer
                            )
                        }
                    }
                }
            }
        }
        if (fighters.isEmpty()) {
            item {
                Text("Keine Kämpfer vorhanden", style = MaterialTheme.typography.titleLarge)
            }
        }
    }

    if (actionClicked) {
        CreateFighter(
            onCreated = {
                onFighterChanged()
                onActionFinished()
            },
            onDismiss = onActionFinished,
            fighters = fighters,
            characterId = characterId,
        )
    }
}