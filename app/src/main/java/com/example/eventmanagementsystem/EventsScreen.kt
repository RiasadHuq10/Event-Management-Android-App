package com.example.eventmanagementsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.eventmanagementsystem.ui.theme.EventManagementSystemTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(navController: NavHostController) {
    Text(
        text = "Event Screen",
    )
    val locations = listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    LazyColumn {
        items(locations) { location ->
            ListItem(
                headlineContent = { Text(location) },
                // go to event page
                modifier = Modifier.clickable {
                    navController.navigate("events/${location}")
                },
            )
            Divider()
        }
    }

}