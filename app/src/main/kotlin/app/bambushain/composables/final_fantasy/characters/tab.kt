package app.bambushain.composables.final_fantasy.characters

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.bambushain.model.Character

@Composable
fun AboutTab(
    character: Character,
    actionClicked: Boolean,
    onUpdated: () -> Unit,
    onActionFinished: () -> Unit,
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        item {
            Column {
                Text("Rasse", style = MaterialTheme.typography.labelLarge)
                Text(character.race.getDisplayName())
            }
        }
        item {
            Column {
                Text("Welt", style = MaterialTheme.typography.labelLarge)
                Text(character.world)
            }
        }
        item {
            Column {
                Text("Datenzentrum", style = MaterialTheme.typography.labelLarge)
                if (character.datacenter != null) {
                    Text(character.datacenter!!)
                } else {
                    Text("Kein Datenzentrum angegeben")
                }
            }
        }
        item {
            Column {
                Text("Freie Gesellschaft", style = MaterialTheme.typography.labelLarge)
                if (character.freeCompany != null) {
                    Text(character.freeCompany!!.name)
                } else {
                    Text("In keiner freien Gesellschaft")
                }
            }
        }
        item {
            Column {
                Text("Rasse", style = MaterialTheme.typography.labelLarge)
                Text(character.race.getDisplayName())
            }
        }
        items(character.customFields ?: emptyList()) {
            Column {
                Text(it.label, style = MaterialTheme.typography.labelLarge)
                Text(it.values.joinToString(", "))
            }
        }
    }
    if (actionClicked) {
        UpdateCharacter(
            onDismiss = onActionFinished,
            onUpdated = onUpdated,
            character = character,
        )
    }
}