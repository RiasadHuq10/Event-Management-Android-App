package com.example.eventmanagementsystem

import KtorClient
import android.annotation.SuppressLint

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SearchBar
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavHostController, search: Boolean, user: Boolean) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    val caroutineScope = rememberCoroutineScope()
    var page by remember { mutableStateOf(1) }

    val event by produceState(initialValue = listOf<Event>(), producer = {
        value = KtorClient.getEvents(listOf("page=1")).events
    })
    var events by remember { mutableStateOf(event) }
    var total by remember { mutableStateOf(-1) }
    var perPage by remember { mutableStateOf(6) }

    Log.d("Page ", page.toString())
    Column {
        if (search) {
            SearchBar(modifier = Modifier.fillMaxWidth(),
                query = searchQuery,
                onQueryChange = { newQuery -> searchQuery = newQuery },
                active = active,
                placeholder = { Text(text = "Search events") },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search, contentDescription = "Search icon"
                    )
                },
                onActiveChange = {
                    active = it
                },
                onSearch = {
                    caroutineScope.launch {
                        val response =
                            KtorClient.getEvents(listOf("page=$page", "search=$searchQuery"))
                        events = response.events
                        total =
                            KtorClient.getEvents(listOf("page=$page", "search=$searchQuery")).total
                        perPage = KtorClient.getEvents(null).perPage
                    }


                    active = false
                },
                content = {},
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        Icon(
                            modifier = Modifier.clickable { searchQuery = "" },
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear search query"
                        )
                    }
                })
        }
        LazyColumn {
            caroutineScope.launch {
                if (search) {
                    val response = KtorClient.getEvents(listOf("page=$page", "search=$searchQuery"))
                    events = response.events
                    total = KtorClient.getEvents(listOf("page=$page", "search=$searchQuery")).total
                    perPage = KtorClient.getEvents(null).perPage
                } else if (user) {
                    events = KtorClient.getVolunteerEvents(listOf("page=$page")).events
                    total = KtorClient.getVolunteerEvents(null).total
                    perPage = 3

                } else {
                    events = KtorClient.getEvents(listOf("page=$page")).events
                    total = KtorClient.getEvents(null).total
                    perPage = KtorClient.getEvents(null).perPage
                }
            }
            Log.d("Total = ", total.toString())
            Log.d("page = ", page.toString())
            items(events) { event ->

                Card(
                    onClick = { navController.navigate("event/${event._id}") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Column {
                        AsyncImage(
                            model = event.image,
                            contentDescription = "Home page Picture",
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(15.dp)
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = event.title,
                            style = TextStyle(fontSize = 20.sp),
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = event.organiser,
                            style = TextStyle(fontSize = 15.sp, color = Color.Gray),
                            textAlign = TextAlign.Center
                        )
                    }
                }

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
                                    if (search) events = KtorClient.getEvents(
                                        listOf(
                                            "page=1", "search=$searchQuery"
                                        )
                                    ).events
                                    else if (user) events =
                                        KtorClient.getVolunteerEvents(listOf("page=1")).events
                                    else events = KtorClient.getEvents(listOf("page=1")).events
                                }
                            },
                            enabled = page != 1,
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
                                            if (search) events = KtorClient.getEvents(
                                                listOf(
                                                    "page=$page", "search=$searchQuery"
                                                )
                                            ).events
                                            else if (user) events =
                                                KtorClient.getVolunteerEvents(listOf("page=$page")).events
                                            else events =
                                                KtorClient.getEvents(listOf("page=$page")).events
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
                                        if (search) events = KtorClient.getEvents(
                                            listOf(
                                                "page=$page", "search=$searchQuery"
                                            )
                                        ).events
                                        else if (user) events =
                                            KtorClient.getVolunteerEvents(listOf("page=$page")).events
                                        else events =
                                            KtorClient.getEvents(listOf("page=$page")).events
                                    }
                                },
                                enabled = false,
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
                                        if (search) events = KtorClient.getEvents(
                                            listOf(
                                                "page=$page", "search=$searchQuery"
                                            )
                                        ).events
                                        else if (user) events =
                                            KtorClient.getVolunteerEvents(listOf("page=$page")).events
                                        else events =
                                            KtorClient.getEvents(listOf("page=$page")).events
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
                                        if (search) events = KtorClient.getEvents(
                                            listOf(
                                                "page=$totalPages", "search=$searchQuery"
                                            )
                                        ).events
                                        else if (user) events =
                                            KtorClient.getVolunteerEvents(listOf("page=$page")).events
                                        else events =
                                            KtorClient.getEvents(listOf("page=$totalPages")).events

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

}






