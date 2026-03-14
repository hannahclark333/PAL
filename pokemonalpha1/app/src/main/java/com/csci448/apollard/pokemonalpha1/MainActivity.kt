package com.csci448.apollard.pokemonalpha1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.csci448.apollard.pokemonalpha1.ui.theme.Pokemonalpha1Theme

data class PokemonData(
    val name: String,
    val hp: Int,
    val attack: Int,
    val defense: Int,
    val spAttack: Int,
    val spDefense: Int,
    val speed: Int,
    val imageRes: Int
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Pokemonalpha1Theme {
                Pokemonalpha1App()
            }
        }
    }
}

enum class AppDestinations(
    val route: String,
    val label: String
) {
    TEAM("team", "Team"),
    INFO("info", "Info"),
    NAME("name", "Name"),
    SETTINGS("settings", "Settings")
}

@PreviewScreenSizes
@Composable
fun Pokemonalpha1App() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: AppDestinations.TEAM.route

    var teamName by rememberSaveable { mutableStateOf("Recommended Team") }
    var selectedPokemonIndex by rememberSaveable { mutableStateOf(0) }

    var notificationsEnabled by rememberSaveable { mutableStateOf(true) }
    var soundEnabled by rememberSaveable { mutableStateOf(true) }
    var darkModeEnabled by rememberSaveable { mutableStateOf(false) }

    val pokemonTeam = remember {
        mutableStateListOf(
            PokemonData("Pikachu", 35, 55, 40, 50, 50, 90, R.drawable.ic_launcher_foreground),
            PokemonData("Charizard", 78, 84, 78, 109, 85, 100, R.drawable.ic_launcher_foreground),
            PokemonData("Blastoise", 79, 83, 100, 85, 105, 78, R.drawable.ic_launcher_foreground),
            PokemonData("Venusaur", 80, 82, 83, 100, 100, 80, R.drawable.ic_launcher_foreground),
            PokemonData("Gengar", 60, 65, 60, 130, 75, 110, R.drawable.ic_launcher_foreground),
            PokemonData("Dragonite", 91, 134, 95, 100, 100, 80, R.drawable.ic_launcher_foreground)
        )
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                item(
                    icon = {
                        when (destination) {
                            AppDestinations.TEAM -> {
                                Icon(Icons.Default.Groups, contentDescription = destination.label)
                            }
                            AppDestinations.INFO -> {
                                Icon(Icons.Default.Info, contentDescription = destination.label)
                            }
                            AppDestinations.NAME -> {
                                Icon(Icons.Default.Edit, contentDescription = destination.label)
                            }
                            AppDestinations.SETTINGS -> {
                                Icon(Icons.Default.Settings, contentDescription = destination.label)
                            }
                        }
                    },
                    label = { Text(destination.label) },
                    selected = currentRoute == destination.route,
                    onClick = {
                        navController.navigate(destination.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                NavHost(
                    navController = navController,
                    startDestination = AppDestinations.TEAM.route
                ) {
                    composable(AppDestinations.TEAM.route) {
                        TeamPanel(
                            teamName = teamName,
                            pokemonTeam = pokemonTeam,
                            onPokemonClick = { index ->
                                selectedPokemonIndex = index
                                navController.navigate(AppDestinations.INFO.route)
                            },
                            onFinalizeClick = {
                                navController.navigate(AppDestinations.NAME.route)
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppDestinations.INFO.route) {
                        InfoPanel(
                            pokemon = pokemonTeam[selectedPokemonIndex],
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppDestinations.NAME.route) {
                        NamePanel(
                            currentTeamName = teamName,
                            onSaveClick = { newTeamName ->
                                teamName = newTeamName
                                navController.navigate(AppDestinations.TEAM.route)
                            },
                            onBackClick = { navController.popBackStack() }
                        )
                    }

                    composable(AppDestinations.SETTINGS.route) {
                        SettingsPanel(
                            notificationsEnabled = notificationsEnabled,
                            onNotificationsChange = { notificationsEnabled = it },
                            soundEnabled = soundEnabled,
                            onSoundChange = { soundEnabled = it },
                            darkModeEnabled = darkModeEnabled,
                            onDarkModeChange = { darkModeEnabled = it },
                            onBackClick = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PalHeader(
    showSettingsButton: Boolean,
    onSettingsClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) {
        Text(
            text = "PAL",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )

        if (showSettingsButton && onSettingsClick != null) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Settings"
                )
            }
        }
    }
}

@Composable
fun TeamPanel(
    teamName: String,
    pokemonTeam: List<PokemonData>,
    onPokemonClick: (Int) -> Unit,
    onFinalizeClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PalHeader(
                showSettingsButton = false,
                onSettingsClick = null
            )

            Text(
                text = teamName,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Recommended Team",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            for (rowIndex in 0 until 3) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    for (columnIndex in 0 until 2) {
                        val pokemonIndex = rowIndex * 2 + columnIndex
                        Button(
                            onClick = { onPokemonClick(pokemonIndex) },
                            modifier = Modifier
                                .weight(1f)
                                .height(72.dp),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(pokemonTeam[pokemonIndex].name)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onFinalizeClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp),
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "Finalize",
                    fontSize = 18.sp
                )
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back")
        }
    }
}

@Composable
fun InfoPanel(
    pokemon: PokemonData,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PalHeader(
                showSettingsButton = false,
                onSettingsClick = null
            )

            Text(
                text = "Pokemon Info",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Image(
                    painter = painterResource(id = pokemon.imageRes),
                    contentDescription = pokemon.name,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = pokemon.name,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Stats:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("HP: ${pokemon.hp}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
            Text("Attack: ${pokemon.attack}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
            Text("Defense: ${pokemon.defense}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
            Text("Special Attack: ${pokemon.spAttack}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
            Text("Special Defense: ${pokemon.spDefense}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
            Text("Speed: ${pokemon.speed}", fontSize = 16.sp, modifier = Modifier.padding(bottom = 6.dp))
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back")
        }
    }
}

@Composable
fun NamePanel(
    currentTeamName: String,
    onSaveClick: (String) -> Unit,
    onBackClick: () -> Unit
) {
    var editedTeamName by rememberSaveable(currentTeamName) { mutableStateOf(currentTeamName) }
    var savedMessage by rememberSaveable { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PalHeader(
                showSettingsButton = false,
                onSettingsClick = null
            )

            Text(
                text = "Name Team",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Name:",
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedTextField(
                    value = editedTeamName,
                    onValueChange = {
                        editedTeamName = it
                        savedMessage = ""
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    onSaveClick(editedTeamName)
                    savedMessage = "Saved"
                }
            ) {
                Text("Save")
            }

            if (savedMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = savedMessage,
                    fontSize = 16.sp
                )
            }
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back")
        }
    }
}

@Composable
fun SettingsPanel(
    notificationsEnabled: Boolean,
    onNotificationsChange: (Boolean) -> Unit,
    soundEnabled: Boolean,
    onSoundChange: (Boolean) -> Unit,
    darkModeEnabled: Boolean,
    onDarkModeChange: (Boolean) -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            PalHeader(
                showSettingsButton = false,
                onSettingsClick = null
            )

            Text(
                text = "Settings",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(20.dp))

            SettingRow(
                label = "Notifications",
                checked = notificationsEnabled,
                onCheckedChange = onNotificationsChange
            )

            SettingRow(
                label = "Sound",
                checked = soundEnabled,
                onCheckedChange = onSoundChange
            )

            SettingRow(
                label = "Dark Mode",
                checked = darkModeEnabled,
                onCheckedChange = onDarkModeChange
            )
        }

        Button(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text("Back")
        }
    }
}

@Composable
fun SettingRow(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 18.sp
        )

        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }
}

@Preview(showBackground = true)
@Composable
fun Pokemonalpha1AppPreview() {
    Pokemonalpha1Theme {
        Pokemonalpha1App()
    }
}