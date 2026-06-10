package app.bambushain.composables.final_fantasy

import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.BackNavigationBehavior
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.window.core.layout.WindowSizeClass
import app.bambushain.api.CharactersApi
import app.bambushain.model.Character
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun Characters(
    charactersApi: CharactersApi = koinInject(),
    fabClicked: Boolean,
    onFabFinished: () -> Unit,
    onHideFab: () -> Unit,
    onShowFab: () -> Unit
) {
    val navigator = rememberListDetailPaneScaffoldNavigator<Character>()
    val coroutineScope = rememberCoroutineScope()
    val windowInfo = currentWindowAdaptiveInfo()
    val isTablet = remember {
        windowInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    }

    var characters by remember { mutableStateOf(emptyList<Character>()) }

    var isLoading by remember { mutableStateOf(true) }
    var firstLoad by remember { mutableStateOf(true) }

    val loadCharacters = suspend {
        isLoading = true
        val res = charactersApi.getCharacters()
        if (res.isSuccessful) {
            characters = res.body()!!
        }
        if (firstLoad && characters.isNotEmpty()) {
            if (isTablet) {
                navigator.navigateTo(
                    ListDetailPaneScaffoldRole.Detail,
                    characters.first()
                )
                onHideFab()
            }
        }
        firstLoad = false
        isLoading = false
    }

    LaunchedEffect(Unit) {
        loadCharacters()
    }
    LaunchedEffect(navigator.currentDestination?.contentKey) {
        if (navigator.currentDestination?.contentKey == null) {
            onShowFab()
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                CharactersList(
                    onCharacterClicked = {
                        coroutineScope.launch {
                            navigator.navigateTo(
                                ListDetailPaneScaffoldRole.Detail,
                                it
                            )
                            if (!isTablet) {
                                onHideFab()
                            }
                        }
                    },
                    characters = characters,
                    onLoadCharacters = {
                        coroutineScope.launch { loadCharacters() }
                    },
                    isLoading = isLoading,
                    fabClicked = fabClicked,
                    onFabFinished = onFabFinished,
                )
            }
        },
        detailPane = {
            AnimatedPane {
                CharacterDetails(
                    character = navigator.currentDestination?.contentKey,
                    showBackButton = navigator.canNavigateBack(),
                    onBackClicked = {
                        coroutineScope.launch {
                            navigator.navigateBack(
                                backNavigationBehavior = BackNavigationBehavior.PopUntilScaffoldValueChange
                            )
                            onShowFab()
                        }
                    },
                    onCharacterUpdated = {
                        coroutineScope.launch {
                            loadCharacters()
                        }
                    },
                    onDeleteCharacter = {
                        coroutineScope.launch {
                            val res = charactersApi.deleteCharacter(it)
                            if (res.isSuccessful) {
                                loadCharacters()
                                navigator.navigateBack()
                            }
                        }
                    }
                )
            }
        }
    )
}