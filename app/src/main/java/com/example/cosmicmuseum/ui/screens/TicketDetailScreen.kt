package com.example.cosmicmuseum.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cosmicmuseum.viewmodel.TicketDetailViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    ticketId: Int,
    viewModel: TicketDetailViewModel
) {
    val ticket by viewModel.ticket.collectAsState()
    var isVisible by remember { mutableStateOf(false) }
    val cosmicBackground = Color(0xFF0B0D17)
    val cosmicAccent = Color(0xFF4FC3F7)

    LaunchedEffect(ticketId) {
        viewModel.loadTicket(ticketId)
        isVisible = true
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        "BOARDING PASS", 
                        style = MaterialTheme.typography.labelLarge.copy(
                            letterSpacing = 4.sp,
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
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
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
            AnimatedVisibility(
                visible = isVisible && ticket != null,
                enter = fadeIn(tween(800)) + slideInVertically(tween(800)) { it / 3 }
            ) {
                ticket?.let { currentTicket ->
                    val statusColor = when (currentTicket.estado) {
                        "Confirmada" -> Color(0xFF00E676)
                        "Pendiente" -> Color(0xFFFFD600)
                        else -> Color(0xFFFF5252)
                    }

                    val fechaFormateada = remember(currentTicket.fechaVisita) {
                        SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(Date(currentTicket.fechaVisita))
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Card Principal Estilo Ticket
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(32.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = Color(0xFF1C2130).copy(alpha = 0.8f)
                            ),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
                        ) {
                            Column {
                                // Cabecera (Status y Misión)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(cosmicAccent.copy(alpha = 0.15f))
                                        .padding(24.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("ESTACIÓN DE SALIDA", style = MaterialTheme.typography.labelSmall, color = cosmicAccent)
                                            Text("COSMOS-X", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
                                        }
                                        Icon(
                                            Icons.Default.RocketLaunch, 
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp),
                                            tint = cosmicAccent
                                        )
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("PUERTA", style = MaterialTheme.typography.labelSmall, color = cosmicAccent)
                                            Text("A-42", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black), color = Color.White)
                                        }
                                    }
                                }

                                // Separador con efecto troquelado
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(24.dp).offset(x = (-12).dp).clip(CircleShape).background(cosmicBackground))
                                    HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = Color.White.copy(alpha = 0.1f),
                                        thickness = 1.dp
                                    )
                                    Box(Modifier.size(24.dp).offset(x = 12.dp).clip(CircleShape).background(cosmicBackground))
                                }

                                // Cuerpo de la información
                                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(24.dp)) {
                                    DetailRow("ASTRONAUTA DESIGNADO", currentTicket.nombreVisitante, Icons.Default.Person, cosmicAccent)
                                    DetailRow("CÓDIGO DE RESERVA", currentTicket.codigoReserva, Icons.Default.VpnKey, cosmicAccent)
                                    
                                    Row(Modifier.fillMaxWidth()) {
                                        Box(Modifier.weight(1f)) {
                                            DetailRow("FECHA ESTELAR", fechaFormateada, Icons.Default.Event, cosmicAccent)
                                        }
                                        Box(Modifier.weight(1f)) {
                                            DetailRow("TRIPULACIÓN", "${currentTicket.cantidadPersonas} PAX", Icons.Default.Group, cosmicAccent)
                                        }
                                    }

                                    // Estado Badge
                                    Surface(
                                        color = statusColor.copy(alpha = 0.1f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth(),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.3f))
                                    ) {
                                        Text(
                                            text = currentTicket.estado.uppercase(),
                                            modifier = Modifier.padding(16.dp),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 2.sp),
                                            color = statusColor,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }

                                    // Total Cost
                                    Column(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalAlignment = Alignment.End
                                    ) {
                                        Text("INVERSIÓN TOTAL", style = MaterialTheme.typography.labelSmall, color = cosmicAccent)
                                        Text(
                                            "S/ ${currentTicket.precioTotal}", 
                                            style = MaterialTheme.typography.headlineMedium.copy(
                                                fontWeight = FontWeight.ExtraBold, 
                                                color = cosmicAccent
                                            )
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Botones de acción final
                        Button(
                            onClick = { navController.navigate("form/${currentTicket.id}") },
                            modifier = Modifier.fillMaxWidth().height(60.dp),
                            shape = RoundedCornerShape(20.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = cosmicAccent, contentColor = Color.Black),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, null)
                            Spacer(Modifier.width(12.dp))
                            Text("RECONFIGURAR MISIÓN", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        TextButton(
                            onClick = { viewModel.deleteTicket(); navController.popBackStack() },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color(0xFFFF5252))
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("ABORTAR RESERVA", style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold))
                        }
                    }
                }
            } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = cosmicAccent)
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, icon: ImageVector, accentColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(20.dp), tint = accentColor)
        Spacer(Modifier.width(16.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color.White.copy(alpha = 0.5f))
            Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold), color = Color.White)
        }
    }
}
