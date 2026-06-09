package app.bambushain.composables.final_fantasy

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowSizeClass
import app.bambushain.R
import app.bambushain.api.CraftersApi
import app.bambushain.api.FightersApi
import app.bambushain.api.FreeCompanyApi
import app.bambushain.api.GatherersApi
import app.bambushain.api.HousingApi
import app.bambushain.composables.final_fantasy.crafter.CrafterTab
import app.bambushain.composables.final_fantasy.fighter.FighterTab
import app.bambushain.composables.final_fantasy.gatherer.GathererTab
import app.bambushain.composables.final_fantasy.housing.HousingTab
import app.bambushain.model.Character
import app.bambushain.model.CharacterHousing
import app.bambushain.model.Crafter
import app.bambushain.model.CrafterJob
import app.bambushain.model.Fighter
import app.bambushain.model.FighterJob
import app.bambushain.model.FreeCompanyHousing
import app.bambushain.model.Gatherer
import app.bambushain.model.GathererJob
import kotlinx.coroutines.joinAll
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

private enum class CharacterDetailsTab {
    About,
    Fighter,
    Crafter,
    Gatherer,
    Housing
}

@Composable
private fun AdaptivePrimaryTabRow(
    selectedTabIndex: Int,
    tabs: @Composable () -> Unit
) {
    val windowInfo = currentWindowAdaptiveInfo()
    val isTablet = remember {
        windowInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)
    }

    if (isTablet) {
        PrimaryTabRow(
            selectedTabIndex = selectedTabIndex,
            tabs = tabs
        )
    } else {
        PrimaryScrollableTabRow(
            selectedTabIndex = selectedTabIndex,
            edgePadding = 8.dp,
            tabs = tabs,
            minTabWidth = 0.dp
        )
    }
}

@Composable
private fun AboutTab(
    character: Character,
    actionClicked: Boolean,
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
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetails(
    character: Character?,
    showBackButton: Boolean,
    onBackClicked: () -> Unit,
    fightersApi: FightersApi = koinInject(),
    craftersApi: CraftersApi = koinInject(),
    gatherersApi: GatherersApi = koinInject(),
    housingApi: HousingApi = koinInject(),
    freeCompanyApi: FreeCompanyApi = koinInject()
) {
    if (character == null) {
        return
    }

    var selectedTab by remember { mutableStateOf(CharacterDetailsTab.About) }
    var actionClicked by remember { mutableStateOf(false) }

    var fighters by remember { mutableStateOf(emptyList<Fighter>()) }
    var crafters by remember { mutableStateOf(emptyList<Crafter>()) }
    var gatherers by remember { mutableStateOf(emptyList<Gatherer>()) }
    var housings by remember { mutableStateOf(emptyList<CharacterHousing>()) }
    var freeCompanyHousing by remember { mutableStateOf<FreeCompanyHousing?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val loadHousings = {
        coroutineScope.launch {
            val res = housingApi.getCharacterHousings(character.id)
            if (res.isSuccessful) {
                housings = res.body()!!
            }
        }
    }
    val loadFighters = {
        coroutineScope.launch {
            val res = fightersApi.getFighters(character.id)
            if (res.isSuccessful) {
                fighters = res.body()!!
            }
        }
    }
    val loadCrafters = {
        coroutineScope.launch {
            val res = craftersApi.getCrafters(character.id)
            if (res.isSuccessful) {
                crafters = res.body()!!
            }
        }
    }
    val loadGatherers = {
        coroutineScope.launch {
            val res = gatherersApi.getGatherers(character.id)
            if (res.isSuccessful) {
                gatherers = res.body()!!
            }
        }
    }
    val loadFreeCompanyHousing = {
        coroutineScope.launch {
            if (character.freeCompany != null) {
                val res = freeCompanyApi.getFreeCompanyHousing(character.freeCompany!!.id)
                if (res.isSuccessful) {
                    freeCompanyHousing = res.body()!!
                }
            }
        }
    }

    LaunchedEffect(character) {
        listOf(
            loadFighters(),
            loadCrafters(),
            loadGatherers(),
            loadHousings(),
            loadFreeCompanyHousing(),
        ).joinAll()
    }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = { Text(character.name) },
                    navigationIcon = {
                        if (showBackButton) {
                            IconButton(onClick = { onBackClicked() }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                    contentDescription = "Zur Charakterübersicht",
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { actionClicked = true }
                        ) {
                            when (selectedTab) {
                                CharacterDetailsTab.About -> Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.pencil),
                                    contentDescription = "Bearbeiten"
                                )

                                CharacterDetailsTab.Fighter -> if (fighters.size != FighterJob.entries.size) Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                                    contentDescription = "Neuer Kämpfer"
                                )

                                CharacterDetailsTab.Crafter -> if (crafters.size != CrafterJob.entries.size) Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                                    contentDescription = "Neuer Handwerker"
                                )

                                CharacterDetailsTab.Gatherer -> if (gatherers.size != GathererJob.entries.size) Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                                    contentDescription = "Neuer Sammler"
                                )

                                CharacterDetailsTab.Housing -> Icon(
                                    imageVector = ImageVector.vectorResource(R.drawable.plus),
                                    contentDescription = "Neue Unterkunft"
                                )
                            }
                        }
                    }
                )
                AdaptivePrimaryTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                ) {
                    Tab(
                        selected = selectedTab == CharacterDetailsTab.About,
                        onClick = { selectedTab = CharacterDetailsTab.About },
                    ) {
                        Text(
                            "Details",
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Tab(
                        selected = selectedTab == CharacterDetailsTab.Fighter,
                        onClick = { selectedTab = CharacterDetailsTab.Fighter },
                    ) {
                        Text(
                            "Kämpfer",
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Tab(
                        selected = selectedTab == CharacterDetailsTab.Crafter,
                        onClick = { selectedTab = CharacterDetailsTab.Crafter },
                    ) {
                        Text(
                            "Handwerker",
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Tab(
                        selected = selectedTab == CharacterDetailsTab.Gatherer,
                        onClick = { selectedTab = CharacterDetailsTab.Gatherer },
                    ) {
                        Text(
                            "Sammler",
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                    Tab(
                        selected = selectedTab == CharacterDetailsTab.Housing,
                        onClick = { selectedTab = CharacterDetailsTab.Housing },
                    ) {
                        Text(
                            "Unterkünfte",
                            modifier = Modifier.padding(8.dp),
                        )
                    }
                }
            }
        },
    ) { innerPadding ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                CharacterDetailsTab.About -> AboutTab(
                    character = character,
                    actionClicked = actionClicked,
                    onActionFinished = { actionClicked = false }
                )

                CharacterDetailsTab.Fighter -> FighterTab(
                    actionClicked = actionClicked,
                    onActionFinished = { actionClicked = false },
                    fighters = fighters,
                    onFighterChanged = { loadFighters() },
                    characterId = character.id
                )

                CharacterDetailsTab.Crafter -> CrafterTab(
                    actionClicked = actionClicked,
                    onActionFinished = { actionClicked = false },
                    crafters = crafters,
                    onCrafterChanged = { loadCrafters() },
                    characterId = character.id
                )

                CharacterDetailsTab.Gatherer -> GathererTab(
                    actionClicked = actionClicked,
                    onActionFinished = { actionClicked = false },
                    gatherers = gatherers,
                    onGathererChanged = { loadGatherers() },
                    characterId = character.id
                )

                CharacterDetailsTab.Housing -> HousingTab(
                    fabClicked = actionClicked,
                    onFabFinished = { actionClicked = false },
                    housings = housings,
                    freeCompanyHousing = freeCompanyHousing,
                    onHousingChanged = { loadHousings() },
                    characterId = character.id
                )
            }
        }
    }
}
