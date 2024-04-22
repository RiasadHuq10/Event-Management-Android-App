package com.example.eventmanagementsystem

import KtorClient
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavHostController,snackbarHostState: SnackbarHostState) {
    // State variables to hold the form data
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var contact by remember { mutableStateOf("") }
    var about by remember { mutableStateOf("") }
    var termsAccepted by remember { mutableStateOf(false) }

    // Dropdown menu state
    val context = LocalContext.current
    val ageGroups = arrayOf("15-18", "18-21", "21-30", "30-50", "50+")
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf(ageGroups[0]) }
    val coroutineScope = rememberCoroutineScope()
    Column(modifier = Modifier.padding(16.dp)) {
        // Email TextField
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Password TextField
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            visualTransformation = PasswordVisualTransformation()
        )

        // Name TextField
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth()
        )

        // Contact TextField
        OutlinedTextField(
            value = contact,
            onValueChange = { contact = it },
            label = { Text("Contact") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        // Age Group Dropdown
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    value = selectedText,
                    onValueChange = {},
                    readOnly = true,
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth() // Modified modifier here
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth() // Modified modifier here
                ) {
                    ageGroups.forEach { group ->
                        DropdownMenuItem(
                            text = { Text(text = group) },
                            onClick = {
                                selectedText = group
                                expanded = false
                                //onAgeGroupChange(group)
                                //Toast.makeText(context, group, Toast.LENGTH_SHORT).show()
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // About Me TextField
        OutlinedTextField(
            value = about,
            onValueChange = { about = it },
            label = { Text("About Me and remark") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = false
        )


        // Terms and Conditions Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = termsAccepted,
                onCheckedChange = { termsAccepted = it }
            )
            Text(
                text = "I agree to the terms and conditions",
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        // Registration Button
        Button(
            onClick = { coroutineScope.launch{
                val response = KtorClient.register(VolunteerRegister(email,password,name,contact,selectedText,about,termsAccepted))
                if (response!= null)
                {
                    snackbarHostState.showSnackbar("Successfully registered")
                    navController.navigate("Home")
                }
                else
                {
                    snackbarHostState.showSnackbar("Error while registering")
                }
            }  },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black, // Your desired background color
                contentColor = Color.White // Your desired content color (for the text/icon)
            ),
            enabled = termsAccepted, // Button is enabled only if terms are accepted
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            shape = RectangleShape
        ) {
            Text("Register")
        }
    }
}