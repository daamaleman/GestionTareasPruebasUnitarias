package ni.edu.uam.gestiontareaspruebasunitarias.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.gestiontareaspruebasunitarias.model.EstadoTarea
import ni.edu.uam.gestiontareaspruebasunitarias.model.Tarea
import ni.edu.uam.gestiontareaspruebasunitarias.repository.GestorTareas

enum class FiltroTarea { TODAS, COMPLETADAS, PENDIENTES }

class TareasViewModel(private val gestorTareas: GestorTareas = GestorTareas()) : ViewModel() {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    private val _pendientesCount = MutableStateFlow(0)
    val pendientesCount: StateFlow<Int> = _pendientesCount.asStateFlow()

    private val _porcentajeProgreso = MutableStateFlow(0f)
    val porcentajeProgreso: StateFlow<Float> = _porcentajeProgreso.asStateFlow()

    private val _filtroActual = MutableStateFlow(FiltroTarea.TODAS)
    val filtroActual: StateFlow<FiltroTarea> = _filtroActual.asStateFlow()

    private val _estaOrdenadoAlfabeticamente = MutableStateFlow(false)
    val estaOrdenadoAlfabeticamente: StateFlow<Boolean> = _estaOrdenadoAlfabeticamente.asStateFlow()

    init {
        actualizarEstado()
    }

    fun agregarTarea(titulo: String, descripcion: String) {
        gestorTareas.agregarTarea(titulo, descripcion)
        actualizarEstado()
    }

    fun eliminarTarea(id: String) {
        gestorTareas.eliminarTarea(id)
        actualizarEstado()
    }

    fun marcarComoCompletada(id: String) {
        gestorTareas.marcarComoCompletada(id)
        actualizarEstado()
    }

    fun cambiarFiltro(nuevoFiltro: FiltroTarea) {
        _filtroActual.value = nuevoFiltro
        actualizarEstado()
    }

    fun alternarOrdenAlfabetico() {
        _estaOrdenadoAlfabeticamente.value = !_estaOrdenadoAlfabeticamente.value
        actualizarEstado()
    }

    private fun actualizarEstado() {
        var listaProcesada = gestorTareas.tareas.toList()

        // Aplicar Filtro
        listaProcesada = when (_filtroActual.value) {
            FiltroTarea.COMPLETADAS -> listaProcesada.filter { it.estado == EstadoTarea.COMPLETADA }
            FiltroTarea.PENDIENTES -> listaProcesada.filter { it.estado == EstadoTarea.PENDIENTE }
            FiltroTarea.TODAS -> listaProcesada
        }

        // Aplicar Orden
        if (_estaOrdenadoAlfabeticamente.value) {
            listaProcesada = listaProcesada.sortedBy { it.titulo.lowercase() }
        }

        _tareas.value = listaProcesada
        _pendientesCount.value = gestorTareas.contarTareasPendientes()
        
        // Calcular Progreso
        val total = gestorTareas.tareas.size
        _porcentajeProgreso.value = if (total > 0) {
            (total - gestorTareas.contarTareasPendientes()).toFloat() / total
        } else {
            0f
        }
    }
}
