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
                            fontWeight = FontWeight.Bold
                        )
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)
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
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = CardDefaults.outlinedCardBorder()
                        ) {
                            Column {
                                // Cabecera (Status y Misión)
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                                        .padding(24.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column {
                                            Text("ESTACIÓN", style = MaterialTheme.typography.labelSmall)
                                            Text("COSMOS-X", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
                                        }
                                        Icon(
                                            Icons.Default.RocketLaunch, 
                                            contentDescription = null,
                                            modifier = Modifier.size(36.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text("GATE", style = MaterialTheme.typography.labelSmall)
                                            Text("A-42", style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Black))
                                        }
                                    }
                                }

                                // Separador con efecto troquelado
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(Modifier.size(24.dp).offset(x = (-12).dp).clip(CircleShape).background(MaterialTheme.colorScheme.background))
                                    HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.3f),
                                        thickness = 1.dp
                                    )
                                    Box(Modifier.size(24.dp).offset(x = 12.dp).clip(CircleShape).background(MaterialTheme.colorScheme.background))
                                }

                                // Cuerpo de la información
                                Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                    DetailRow("ASTRONAUTA", currentTicket.nombreVisitante, Icons.Default.Person)
                                    DetailRow("ID DE ACCESO", currentTicket.codigoReserva, Icons.Default.VpnKey)
                                    
                                    Row(Modifier.fillMaxWidth()) {
                                        Box(Modifier.weight(1f)) {
                                            DetailRow("FECHA", fechaFormateada, Icons.Default.Event)
                                        }
                                        Box(Modifier.weight(1f)) {
                                            DetailRow("PASAJEROS", "${currentTicket.cantidadPersonas}", Icons.Default.Group)
                                        }
                                    }

                                    // Estado Badge
                                    Surface(
                                        color = statusColor.copy(alpha = 0.15f),
                                        shape = RoundedCornerShape(12.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = "ESTADO: ${currentTicket.estado.uppercase()}",
                                            modifier = Modifier.padding(12.dp),
                                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold, letterSpacing = 1.sp),
                                            color = statusColor,
                                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                        )
                                    }

                                    // Total Cost
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.Bottom
                                    ) {
                                        Text("INVERSIÓN TOTAL", style = MaterialTheme.typography.labelSmall)
                                        Text("S/ ${currentTicket.precioTotal}", style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.primary))
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
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                        ) {
                            Icon(Icons.Default.Edit, null)
                            Spacer(Modifier.width(8.dp))
                            Text("RECONFIGURAR MISIÓN", fontWeight = FontWeight.Bold)
                        }

                        TextButton(
                            onClick = { viewModel.deleteTicket(); navController.popBackStack() },
                            modifier = Modifier.padding(top = 12.dp),
                            colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(18.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("Abortar Reserva")
                        }
                    }
                }
            } ?: Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun DetailRow(label: String, value: String, icon: ImageVector) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(icon, null, modifier = Modifier.size(18.dp), tint = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.width(12.dp))
        Column {
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.secondary)
            Text(value, style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
        }
    }
}
