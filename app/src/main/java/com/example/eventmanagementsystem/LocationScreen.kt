package com.example.eventmanagementsystem

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection.Companion.Content
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min

@Composable
fun LocationScreen(navController: NavHostController, location: String?) {
    val caroutineScope = rememberCoroutineScope()
    var page by remember { mutableStateOf(1) }
    val event by produceState(initialValue = listOf<Event>(), producer = {
        value = KtorClient.getEvents(listOf("page=1", "location=$location")).events
    })
    var events by remember { mutableStateOf(event) }
    var total by remember { mutableStateOf(-1) }
    var perPage by remember { mutableStateOf(-6) }
    LazyColumn {
        caroutineScope.launch {
            events = KtorClient.getEvents(listOf("page=$page", "location=$location")).events
            total = KtorClient.getEvents(listOf("page=$page", "location=$location")).total
            perPage = KtorClient.getEvents(listOf("page=$page", "location=$location")).perPage
        }
        items(events) { event ->
            ListItem(
                headlineContent = { Text(event.title) },
                modifier = Modifier.clickable {
                    navController.navigate("event/${event._id}")
                },

                )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {
                Text(
                    text = event.location,
                    style = TextStyle(fontSize = 15.sp, color = Color.Gray),
                    textAlign = TextAlign.Center
                )
            }
            Divider()
        }
        val totalPages = ceil((total.toDouble()) / (perPage.toDouble())).toInt()
        item {
            if (totalPages > 1) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            //currentPage = 1

                            caroutineScope.launch {
                                page = 1
                                events = KtorClient.getEvents(
                                    listOf(
                                        "page=1", "location=$location"
                                    )
                                ).events
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black, // Your desired background color
                            contentColor = Color.White // Your desired content color (for the text/icon)
                        ),
                        shape = RectangleShape,
                    ) {

                        Text("1")
                    }
                    if (page != 1) {
                        if (page > 2) {
                            Button(
                                onClick = {
                                    caroutineScope.launch {
                                        page = page - 1
                                        events = KtorClient.getEvents(
                                            listOf(
                                                "page=$page", "location=$location"
                                            )
                                        ).events
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.Black, // Your desired background color
                                    contentColor = Color.White // Your desired content color (for the text/icon)
                                ),
                                shape = RectangleShape,
                            ) {

                                Text("<")
                            }
                        }

                        Button(
                            onClick = {

                                caroutineScope.launch {
                                    page = page
                                    events = KtorClient.getEvents(
                                        listOf(
                                            "page=$page", "location=$location"
                                        )
                                    ).events
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Your desired background color
                                contentColor = Color.White // Your desired content color (for the text/icon)
                            ),
                            shape = RectangleShape,
                        ) {

                            Text("$page")
                        }
                    }
                    if (page < totalPages - 1) {
                        Button(
                            onClick = {

                                caroutineScope.launch {
                                    page = page + 1
                                    events = KtorClient.getEvents(
                                        listOf(
                                            "page=$page", "location=$location"
                                        )
                                    ).events
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Your desired background color
                                contentColor = Color.White // Your desired content color (for the text/icon)
                            ),
                            shape = RectangleShape,
                        ) {

                            Text(">")
                        }

                    }
                    if (page != totalPages) {
                        Button(
                            onClick = {
                                caroutineScope.launch {
                                    page = totalPages
                                    events = KtorClient.getEvents(
                                        listOf(
                                            "page=$totalPages", "location=$location"
                                        )
                                    ).events

                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Your desired background color
                                contentColor = Color.White // Your desired content color (for the text/icon)
                            ),
                            shape = RectangleShape,
                        ) {

                            Text("$totalPages")
                        }
                    }

                }


            }
        }

    }

}