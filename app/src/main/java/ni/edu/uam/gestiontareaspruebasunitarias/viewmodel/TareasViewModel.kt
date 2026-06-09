package ni.edu.uam.gestiontareaspruebasunitarias.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ni.edu.uam.gestiontareaspruebasunitarias.model.Tarea
import ni.edu.uam.gestiontareaspruebasunitarias.repository.GestorTareas

class TareasViewModel(private val gestorTareas: GestorTareas = GestorTareas()) : ViewModel() {

    private val _tareas = MutableStateFlow<List<Tarea>>(emptyList())
    val tareas: StateFlow<List<Tarea>> = _tareas.asStateFlow()

    private val _pendientesCount = MutableStateFlow(0)
    val pendientesCount: StateFlow<Int> = _pendientesCount.asStateFlow()

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

    private fun actualizarEstado() {
        _tareas.value = gestorTareas.tareas.toList() // Copia de la lista para disparar el StateFlow
        _pendientesCount.value = gestorTareas.contarTareasPendientes()
    }
}
