package app.bambushain.composables.final_fantasy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.bambushain.model.Character

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersList(
    onCharacterClicked: (character: Character) -> Unit,
    characters: List<Character>,
    onLoadCharacters: () -> Unit,
    isLoading: Boolean,
    onFabFinished: () -> Unit,
    fabClicked: Boolean,
) {
    val pullToRefreshState = rememberPullToRefreshState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Charaktere") },
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = isLoading,
            onRefresh = onLoadCharacters,
            state = pullToRefreshState,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                items(characters) {
                    ElevatedCard(
                        onClick = { onCharacterClicked(it) },
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            Text(it.name, style = MaterialTheme.typography.titleLarge)
                            Text(it.race.getDisplayName())
                            if (it.freeCompany != null) {
                                Text("Mitglied in " + it.freeCompany!!.name)
                            } else {
                                Text("In keiner Freien Gesellschaft")
                            }
                            Text("In der Welt " + it.world)
                        }
                    }
                }
            }
        }
    }
}