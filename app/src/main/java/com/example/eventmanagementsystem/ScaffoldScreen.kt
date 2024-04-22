package com.example.eventmanagementsystem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import okhttp3.internal.wait

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldScreen() {
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedItem by remember { mutableIntStateOf(0) }
    var items by remember { mutableStateOf(listOf("Home", "Events", "Search", "Login")) }
    if (loggedIn) {
        items = listOf("Home", "Events", "Search", "User")
    }
    val navController = rememberNavController()
    var topBar by remember {
        mutableStateOf("")
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }, topBar = {
        TopAppBar(
            title = { Text(text = topBar, color = Color.White) }, // Set text color to white
            colors = topAppBarColors(
                containerColor = Color.Black,
                titleContentColor = Color.White,

                ),
            navigationIcon = {
                if (!topBar.equals("")) {
                    IconButton(onClick = { navController.popBackStack() }) {

                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }

            },
        )
    }, bottomBar = {
        NavigationBar(
            containerColor = Color.Black
        ) {
            items.forEachIndexed { index, item ->
                NavigationBarItem(


                    icon = {
                        CustomIcon(label = item, isSelected = selectedItem == index)
                    },

                    selected = false,
                    onClick = {
                        selectedItem = index
                        navController.navigate(items[selectedItem]) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    },

                    )
            }
        }
    }, content = { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding),
        ) {

            NavHost(
                navController = navController,
                startDestination = "Home",
            ) {
                composable("Home") {
                    topBar = ""
                    selectedItem = 0
                    if (loggedIn) {
                        items = listOf("Home", "Events", "Search", "User")

                    } else {
                        items = listOf("Home", "Events", "Search", "Login")

                    }
                    HomeScreen(navController, false, false)
                }

                composable("Events") {
                    selectedItem = 1
                    topBar = "Events"
                    EventsScreen(navController)
                }
                composable("events/{location}") { backStackEntry ->
                    topBar = backStackEntry.arguments?.getString("location") + " Address"
                    LocationScreen(
                        navController, backStackEntry.arguments?.getString("location")
                    )
                }
                composable("Search") {
                    selectedItem = 2
                    topBar = "Events"
                    HomeScreen(navController, true, false)
                }
                composable("Login") {
                    selectedItem = 3
                    topBar = "Login"
                    if (!loggedIn) {
                        items = listOf("Home", "Events", "Search", "Login")
                    }
                    LoginScreen(navController, snackbarHostState, items)

                }
                composable("User") {
                    selectedItem = 3
                    topBar = "Registered Events"
                    if (loggedIn) {
                        items = listOf("Home", "Events", "Search", "User")
                    }
                    HomeScreen(navController, false, true)
                }
                composable("register") {
                    topBar = "Become Volunteer"
                    RegisterScreen(navController, snackbarHostState)
                }
                composable("event/{eventId}") { backStackEntry ->

                    val eventResponse by produceState(initialValue = Event(
                        "Loading",
                        "Loading",
                        "Loading",
                        "Loading",
                        "Loading",
                        "Loading",
                        "Loading",
                        -1,
                        false,
                        "Loading",
                        "Loading",
                        null

                    ), producer = {
                        value = KtorClient.getEvent(backStackEntry.arguments?.getString("eventId"))
                    })
                    topBar = eventResponse.title
                    EventScreen(
                        backStackEntry.arguments?.getString("eventId"),
                        snackbarHostState,
                        eventResponse
                    )
                }

            }
        }
    })
}

@Composable
fun RowScope.CustomIcon(label: String, isSelected: Boolean) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        Text(
            text = label,
            color = if (isSelected) Color.Cyan else Color.White,
            modifier = Modifier.align(Alignment.Center)
        )

        if (isSelected) {
            Divider(
                color = Color.Cyan,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .height(2.dp),
                thickness = 2.dp
            )
        }
    }
}