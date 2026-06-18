package com.example.cosmicmuseum.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.cosmicmuseum.viewmodel.EventsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    navController: NavController,
    viewModel: EventsViewModel
) {

    val nasaData by viewModel.nasaData.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadApod()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Evento Astronómico")
                }
            )
        }
    ) { paddingValues ->

        when {

            isLoading -> {

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {

                    CircularProgressIndicator()
                }
            }

            error != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                ) {

                    Text(
                        text = "Error: $error"
                    )
                }
            }

            nasaData != null -> {

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {

                    AsyncImage(
                        model = nasaData!!.url,
                        contentDescription = nasaData!!.title,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Text(
                        text = nasaData!!.title,
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = nasaData!!.date
                    )

                    Text(
                        text = nasaData!!.explanation
                    )
                }
            }
        }
    }
}