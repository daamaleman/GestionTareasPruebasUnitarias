package ni.edu.uam.gestiontareaspruebasunitarias.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import ni.edu.uam.gestiontareaspruebasunitarias.model.EstadoTarea
import ni.edu.uam.gestiontareaspruebasunitarias.model.Tarea
import ni.edu.uam.gestiontareaspruebasunitarias.ui.theme.DeepDarkBackground
import ni.edu.uam.gestiontareaspruebasunitarias.ui.theme.DeepDarkSurface
import ni.edu.uam.gestiontareaspruebasunitarias.viewmodel.FiltroTarea
import ni.edu.uam.gestiontareaspruebasunitarias.viewmodel.TareasViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun PantallaTareas(viewModel: TareasViewModel = viewModel()) {
    val tareas by viewModel.tareas.collectAsState()
    val porcentajeProgreso by viewModel.porcentajeProgreso.collectAsState()
    val filtroActual by viewModel.filtroActual.collectAsState()
    val estaOrdenado by viewModel.estaOrdenadoAlfabeticamente.collectAsState()

    var nuevoTitulo by remember { mutableStateOf("") }
    val progressAnimate by animateFloatAsState(
        targetValue = porcentajeProgreso,
        animationSpec = spring(),
        label = "progress"
    )

    Scaffold(
        containerColor = DeepDarkBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Mis Tareas",
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp,
                        color = Color.White
                    )
                },
                actions = {
                    IconButton(onClick = { viewModel.alternarOrdenAlfabetico() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Sort,
                            contentDescription = "Ordenar",
                            tint = if (estaOrdenado) MaterialTheme.colorScheme.primary else Color.Gray
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            // Card de Progreso
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = CardDefaults.cardColors(containerColor = DeepDarkSurface),
                shape = RoundedCornerShape(24.dp)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "Progreso General",
                            color = Color.White.copy(alpha = 0.7f),
                            style = MaterialTheme.typography.labelLarge
                        )
                        Text(
                            "${(porcentajeProgreso * 100).toInt()}%",
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { progressAnimate },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(10.dp)),
                        color = MaterialTheme.colorScheme.primary,
                        trackColor = Color.White.copy(alpha = 0.1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Campo de entrada estilizado
            OutlinedTextField(
                value = nuevoTitulo,
                onValueChange = { nuevoTitulo = it },
                placeholder = { Text("¿Qué tienes pendiente?", color = Color.Gray) },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("textFieldTarea"),
                shape = RoundedCornerShape(16.dp),
                trailingIcon = {
                    if (nuevoTitulo.isNotBlank()) {
                        IconButton(onClick = {
                            viewModel.agregarTarea(nuevoTitulo, "")
                            nuevoTitulo = ""
                        }) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Agregar",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.1f),
                    focusedContainerColor = DeepDarkSurface,
                    unfocusedContainerColor = DeepDarkSurface,
                    cursorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Filtros Minimalistas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FiltroTarea.entries.forEach { filtro ->
                    val seleccionado = filtroActual == filtro
                    Surface(
                        onClick = { viewModel.cambiarFiltro(filtro) },
                        shape = RoundedCornerShape(12.dp),
                        color = if (seleccionado) MaterialTheme.colorScheme.primary else DeepDarkSurface,
                        contentColor = if (seleccionado) Color.Black else Color.Gray,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.padding(vertical = 8.dp)) {
                            Text(
                                filtro.name.lowercase().replaceFirstChar { it.uppercase() },
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = if (seleccionado) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Tareas con Animaciones
            Box(modifier = Modifier.fillMaxSize()) {
                if (tareas.isEmpty()) {
                    EmptyState()
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .testTag("listaTareas"),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 20.dp)
                    ) {
                        items(tareas, key = { it.id }) { tarea ->
                            ItemTarea(
                                modifier = Modifier.animateItem(),
                                tarea = tarea,
                                onMarcarCompletada = { viewModel.marcarComoCompletada(tarea.id) },
                                onEliminar = { viewModel.eliminarTarea(tarea.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ItemTarea(
    modifier: Modifier = Modifier,
    tarea: Tarea,
    onMarcarCompletada: () -> Unit,
    onEliminar: () -> Unit
) {
    val estaCompletada = tarea.estado == EstadoTarea.COMPLETADA
    
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (estaCompletada) DeepDarkSurface.copy(alpha = 0.5f) else DeepDarkSurface
        ),
        shape = RoundedCornerShape(20.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onMarcarCompletada) {
                Icon(
                    imageVector = if (estaCompletada) Icons.Rounded.CheckCircle else Icons.Rounded.RadioButtonUnchecked,
                    contentDescription = "Estado",
                    tint = if (estaCompletada) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.size(28.dp)
                )
            }
            
            Column(modifier = Modifier
                .weight(1f)
                .padding(start = 8.dp)) {
                Text(
                    text = tarea.titulo,
                    color = if (estaCompletada) Color.Gray else Color.White,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium,
                    textDecoration = if (estaCompletada) TextDecoration.LineThrough else null
                )
            }

            IconButton(
                onClick = onEliminar,
                modifier = Modifier.testTag("botonEliminar_${tarea.id}")
            ) {
                Icon(
                    imageVector = Icons.Default.DeleteOutline,
                    contentDescription = "Eliminar",
                    tint = Color.Red.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 50.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            Icons.AutoMirrored.Filled.Assignment,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = Color.White.copy(alpha = 0.05f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "No hay tareas por aquí",
            color = Color.White.copy(alpha = 0.2f),
            fontWeight = FontWeight.Medium
        )
    }
}
