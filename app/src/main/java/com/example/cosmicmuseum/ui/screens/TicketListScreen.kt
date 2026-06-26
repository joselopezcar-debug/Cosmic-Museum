package com.example.cosmicmuseum.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.viewmodel.AuthViewModel
import com.example.cosmicmuseum.viewmodel.TicketListViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavController,
    viewModel: TicketListViewModel,
    authViewModel: AuthViewModel
) {
    val tickets by viewModel.tickets.collectAsState()
    val cosmicBackground = Color(0xFF0B0D17)
    val cosmicAccent = Color(0xFF4FC3F7) // Brighter blue for visibility

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Column {
                        Text(
                            "BITÁCORA ESPACIAL",
                            style = MaterialTheme.typography.headlineMedium.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 2.sp,
                                color = Color.White
                            )
                        )
                        Text(
                            "Centro de Misiones Cosmic Museum",
                            style = MaterialTheme.typography.bodySmall,
                            color = cosmicAccent // Changed from primary
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { navController.navigate("events") },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Default.RocketLaunch,
                            contentDescription = "Explorar",
                            tint = cosmicAccent // Changed from primary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {
                            authViewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0)
                            }
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.1f))
                    ) {
                        Icon(
                            Icons.Default.Logout,
                            contentDescription = "Cerrar sesión",
                            tint = Color(0xFFFF5252) // Explicit bright red
                        )
                    }
                },
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = cosmicBackground.copy(alpha = 0.95f)
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navController.navigate("form") },
                containerColor = cosmicAccent, // Changed from primary
                contentColor = Color.Black,
                shape = RoundedCornerShape(20.dp),
                elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 8.dp)
            ) {
                Icon(Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text("NUEVA MISIÓN", fontWeight = FontWeight.Bold)
            }
        },
        containerColor = cosmicBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            cosmicBackground,
                            Color(0xFF14172B)
                        )
                    )
                )
        ) {
            if (tickets.isEmpty()) {
                EmptyStateUI(cosmicAccent)
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(tickets) { index, ticket ->
                        AnimatedTicketItem(index, ticket, cosmicAccent) {
                            navController.navigate("detail/${ticket.id}")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedTicketItem(index: Int, ticket: TicketEntity, accentColor: Color, onClick: () -> Unit) {
    var isVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { isVisible = true }

    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn(tween(600, delayMillis = index * 80)) +
                slideInVertically(tween(600, delayMillis = index * 80)) { it / 2 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick() },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFF1C2130).copy(alpha = 0.7f)
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(52.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = accentColor.copy(alpha = 0.15f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = when (ticket.tipoEntrada) {
                                "VIP" -> Icons.Default.Stars
                                "Estudiante" -> Icons.Default.School
                                "Evento Especial" -> Icons.Default.AutoAwesome
                                else -> Icons.Default.ConfirmationNumber
                            },
                            contentDescription = null,
                            tint = accentColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(Modifier.width(16.dp))

                Column(Modifier.weight(1f)) {
                    Text(
                        ticket.nombreVisitante,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Text(
                        ticket.codigoReserva,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.White.copy(alpha = 0.6f)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        "S/ ${ticket.precioTotal}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Black,
                            color = accentColor
                        )
                    )

                    val statusColor = when (ticket.estado) {
                        "Confirmada" -> Color(0xFF00C853)
                        "Pendiente" -> Color(0xFFFFAB00)
                        else -> Color(0xFFFF3D00)
                    }

                    Surface(
                        color = statusColor.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                    ) {
                        Text(
                            ticket.estado.uppercase(),
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = statusColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyStateUI(accentColor: Color) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.Default.Explore,
            null,
            modifier = Modifier.size(80.dp),
            tint = accentColor.copy(alpha = 0.2f)
        )
        Spacer(Modifier.height(16.dp))
        Text(
            "SIN MISIONES ACTIVAS",
            style = MaterialTheme.typography.labelLarge,
            color = Color.White.copy(alpha = 0.4f),
            letterSpacing = 2.sp
        )
    }
}
