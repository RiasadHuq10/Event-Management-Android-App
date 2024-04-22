package com.example.eventmanagementsystem

import KtorClient
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
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
import coil.compose.AsyncImage
import com.example.eventmanagementsystem.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(id: String?, snackbarHostState: SnackbarHostState, eventResponse: Event) {

    var event by remember {
        mutableStateOf(eventResponse)
    }
    val whiteBorder = Color.White
    val coroutineScope = rememberCoroutineScope()
    var joined by remember { mutableStateOf(false) }
    //var total by remember { mutableStateOf(0)}
    var volunterEvents by remember {
        mutableStateOf(listOf(""))
    }
    LazyColumn {
        if (loggedIn) {
            coroutineScope.launch {
                //total = KtorClient.getVolunteerEvents(null).total

                event = KtorClient.getEvent(id)
                volunterEvents = KtorClient.getVolunteerEvents().events
                for (e in volunterEvents) {
                    if (id != null) {
                        Log.d("Event ID", id)
                    }
                    if (id != null) {
                        Log.d("VolEvent ID", e)
                    }
                    if (e.equals(id)) {
                        joined = true
                        break
                    }
                }

            }
        } else {
            coroutineScope.launch {

                event = KtorClient.getEvent(id)

            }
        }

        item {

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)

            ) {
                Column {
                    AsyncImage(
                        model = event.image,
                        contentDescription = "Event Picture",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),

                        )

                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Column {
                    Text(
                        text = event.title,
                        style = TextStyle(fontSize = 27.sp),
                        modifier = Modifier.padding(top = 5.dp, bottom = 5.dp)

                    )
                    Text(
                        text = event.organiser,
                        style = TextStyle(fontSize = 18.sp, color = Color.Gray),
                        modifier = Modifier.padding(bottom = 5.dp)
                    )
                    Text(
                        text = event.description,
                        style = TextStyle(fontSize = 20.sp),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Divider()

                    Text(
                        text = "Date: " + event.event_date,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)

                    )

                    Divider()

                    Text(
                        text = "Location: " + event.location,
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)
                    )

                    Divider()

                    Text(
                        text = "Quota: " + event.quota.toString(),
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = TextStyle(fontSize = 20.sp)
                    )
                    Divider()
                }
            }
            if (loggedIn) {

                Box(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    if (joined) {
                                        val res = KtorClient.unregisterEvent(event._id)
                                        if (res != null) {
                                            snackbarHostState.showSnackbar(res)
                                            joined = false
                                        } else {
                                            snackbarHostState.showSnackbar("Error while unregistering")
                                        }

                                        event = KtorClient.getEvent(id)

                                    } else {
                                        val res = KtorClient.joinEvent(event._id)
                                        if (res != null) {
                                            snackbarHostState.showSnackbar(res)
                                            joined = true
                                        } else {
                                            snackbarHostState.showSnackbar("Error. Quota is full.")
                                        }

                                        event = KtorClient.getEvent(id)
                                    }

                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color.Black, // Your desired background color
                                contentColor = Color.White // Your desired content color (for the text/icon)
                            ),
                            shape = RectangleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp)

                        ) {
                            if (joined) Text("Unregister")
                            else Text("Join Event")
                        }
                    }
                }
            }
        }
    }
}