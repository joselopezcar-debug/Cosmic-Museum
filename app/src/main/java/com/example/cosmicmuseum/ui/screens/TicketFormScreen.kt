package com.example.cosmicmuseum.ui.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.cosmicmuseum.data.local.TicketEntity
import com.example.cosmicmuseum.notifications.TicketNotification
import com.example.cosmicmuseum.viewmodel.TicketFormViewModel
import java.text.SimpleDateFormat
import java.util.Locale

private fun calcularPrecio(tipoEntrada: String, cantidad: Int): Double {
    val precioUnitario = when (tipoEntrada) {
        "General" -> 25.0
        "Estudiante" -> 15.0
        "VIP" -> 50.0
        "Evento Especial" -> 80.0
        else -> 0.0
    }
    return precioUnitario * cantidad
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketFormScreen(
    navController: NavController,
    ticketId: Int?,
    viewModel: TicketFormViewModel
) {
    val currentTicket by viewModel.currentTicket.collectAsState()
    
    var nombreVisitante by remember { mutableStateOf("") }
    var fechaVisita by remember { mutableStateOf("") }
    var tipoEntrada by remember { mutableStateOf("General") }
    var cantidadPersonas by remember { mutableStateOf("1") }
    var estado by remember { mutableStateOf("Pendiente") }

    val tiposEntrada = listOf("General", "Estudiante", "VIP", "Evento Especial")
    val estados = listOf("Pendiente", "Confirmada", "Cancelada")
    var expandedTipo by remember { mutableStateOf(false) }
    var expandedEstado by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(ticketId) { if (ticketId != null) viewModel.loadTicket(ticketId) }
    LaunchedEffect(currentTicket) {
        currentTicket?.let {
            nombreVisitante = it.nombreVisitante
            fechaVisita = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(it.fechaVisita)
            tipoEntrada = it.tipoEntrada
            cantidadPersonas = it.cantidadPersonas.toString()
            estado = it.estado
        }
    }

    val precioCalculado = calcularPrecio(tipoEntrada, cantidadPersonas.toIntOrNull() ?: 0)

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CONFIGURAR MISIÓN", style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 2.sp)) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.Close, null)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = Color.Transparent)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                // Sección: Identificación
                Text("DATOS DEL COMANDANTE", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                
                MissionTextField(nombreVisitante, { nombreVisitante = it }, "Nombre del Astronauta", Icons.Default.Person)
                MissionTextField(fechaVisita, { fechaVisita = it }, "Fecha Estelar (dd/MM/yyyy)", Icons.Default.Event)

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(modifier = Modifier.weight(1f)) {
                        ExposedDropdownMenuBox(expanded = expandedTipo, onExpandedChange = { expandedTipo = !expandedTipo }) {
                            OutlinedTextField(
                                value = tipoEntrada,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Membresía") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedTipo) },
                                modifier = Modifier.menuAnchor().fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp)
                            )
                            ExposedDropdownMenu(expanded = expandedTipo, onDismissRequest = { expandedTipo = false }) {
                                tiposEntrada.forEach { op ->
                                    DropdownMenuItem(text = { Text(op) }, onClick = { tipoEntrada = op; expandedTipo = false })
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.width(100.dp)) {
                        MissionTextField(cantidadPersonas, { cantidadPersonas = it }, "Cant.", Icons.Default.Group)
                    }
                }

                ExposedDropdownMenuBox(expanded = expandedEstado, onExpandedChange = { expandedEstado = !expandedEstado }) {
                    OutlinedTextField(
                        value = estado,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Estado de la Misión") },
                        leadingIcon = { Icon(Icons.Default.SettingsSuggest, null) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedEstado) },
                        modifier = Modifier.menuAnchor().fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp)
                    )
                    ExposedDropdownMenu(expanded = expandedEstado, onDismissRequest = { expandedEstado = false }) {
                        estados.forEach { op ->
                            DropdownMenuItem(text = { Text(op) }, onClick = { estado = op; expandedEstado = false })
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Tarjeta de Presupuesto con Glassmorphism
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                    border = CardDefaults.outlinedCardBorder()
                ) {
                    Row(
                        modifier = Modifier.padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("CRÉDITOS TOTALES", style = MaterialTheme.typography.labelSmall)
                            Text("S/ $precioCalculado", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Black, color = MaterialTheme.colorScheme.primary))
                        }
                        Icon(Icons.Default.MonetizationOn, null, modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.primary)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val ticket = TicketEntity(
                            id = ticketId ?: 0,
                            nombreVisitante = nombreVisitante,
                            fechaVisita = try { SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaVisita)?.time ?: 0L } catch(e: Exception) { 0L },
                            tipoEntrada = tipoEntrada,
                            cantidadPersonas = cantidadPersonas.toIntOrNull() ?: 1,
                            codigoReserva = currentTicket?.codigoReserva ?: "CM-${System.currentTimeMillis()}",
                            precioTotal = precioCalculado,
                            estado = estado
                        )
                        viewModel.saveTicket(ticket)

                        TicketNotification.showNotification(
                            context,
                            "Reserva registrada",
                            "Tu misión ${ticket.codigoReserva} fue creada"
                        )

                        navController.popBackStack()
                    },
                    modifier = Modifier.fillMaxWidth().height(64.dp),
                    shape = RoundedCornerShape(20.dp)
                ) {
                    Text("CONFIRMAR LANZAMIENTO", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                }
            }
        }
    }
}

@Composable
fun MissionTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, modifier = Modifier.size(20.dp)) },
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        singleLine = true
    )
}
