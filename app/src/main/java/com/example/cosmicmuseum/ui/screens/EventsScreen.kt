package com.example.cosmicmuseum.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    val cosmicBackground = Color(0xFF0B0D17)
    val cosmicAccent = Color(0xFF4FC3F7) // Azul brillante para contraste

    LaunchedEffect(Unit) {
        viewModel.loadApod()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        "EXPLORACIÓN ESPACIAL", 
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 2.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = cosmicBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (isLoading) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(48.dp),
                        strokeWidth = 3.dp,
                        color = cosmicAccent
                    )
                }
            }

            nasaData?.let { data ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Header Image
                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp)) {
                        AsyncImage(
                            model = data.url,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            cosmicBackground.copy(alpha = 0.6f),
                                            cosmicBackground
                                        ),
                                        startY = 250f
                                    )
                                )
                        )
                    }

                    // Content Section
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 24.dp)
                            .offset(y = (-40).dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(24.dp),
                            color = Color(0xFF1C2130).copy(alpha = 0.9f),
                            tonalElevation = 4.dp,
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                        ) {
                            Column(modifier = Modifier.padding(24.dp)) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        Icons.Default.RocketLaunch, 
                                        null, 
                                        tint = cosmicAccent, 
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        data.date, 
                                        style = MaterialTheme.typography.labelMedium, 
                                        color = cosmicAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                                Spacer(Modifier.height(12.dp))
                                Text(
                                    text = data.title,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontWeight = FontWeight.Black,
                                        lineHeight = 32.sp
                                    ),
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Text(
                            text = "INFORME DE MISIÓN",
                            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp),
                            color = cosmicAccent,
                            modifier = Modifier.padding(bottom = 16.dp)
                        )

                        Text(
                            text = data.explanation,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                lineHeight = 26.sp,
                                textAlign = TextAlign.Justify,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        )
                        
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}
