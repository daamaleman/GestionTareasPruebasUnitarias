package ni.edu.uam.gestiontareaspruebasunitarias.ui

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createComposeRule
import ni.edu.uam.gestiontareaspruebasunitarias.ui.theme.GestionTareasPruebasUnitariasTheme
import org.junit.Rule
import org.junit.Test

class PantallaTareasTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun agregarTarea_apareceEnLista() {
        composeTestRule.setContent {
            GestionTareasPruebasUnitariasTheme {
                PantallaTareas()
            }
        }

        val nombreTarea = "Aprender Testing"

        // 1. Escribir en el campo
        composeTestRule.onNodeWithTag("textFieldTarea").performTextInput(nombreTarea)
        
        // 2. Hacer clic en agregar
        composeTestRule.onNodeWithTag("botonAgregarTarea").performClick()

        // 3. Validar que aparece en pantalla
        composeTestRule.onNodeWithText(nombreTarea).assertIsDisplayed()
    }

    @Test
    fun botonAgregar_estaHabilitadoYResponde() {
        composeTestRule.setContent {
            GestionTareasPruebasUnitariasTheme {
                PantallaTareas()
            }
        }

        composeTestRule.onNodeWithTag("botonAgregarTarea")
            .assertExists()
            .assertHasClickAction()
    }

    @Test
    fun eliminarTarea_desapareceDePantalla() {
        composeTestRule.setContent {
            GestionTareasPruebasUnitariasTheme {
                PantallaTareas()
            }
        }

        val tareaAEliminar = "Tarea para borrar"
        
        // Agregar la tarea
        composeTestRule.onNodeWithTag("textFieldTarea").performTextInput(tareaAEliminar)
        composeTestRule.onNodeWithTag("botonAgregarTarea").performClick()
        composeTestRule.onNodeWithText(tareaAEliminar).assertIsDisplayed()

        // Hacer clic en eliminar (usando content description ya que el tag es dinámico con ID)
        composeTestRule.onNodeWithContentDescription("Eliminar").performClick()

        // Validar que el nodo ya no existe
        composeTestRule.onNodeWithText(tareaAEliminar).assertDoesNotExist()
    }

    @Test
    fun contadorPendientes_seActualizaCorrectamente() {
        composeTestRule.setContent {
            GestionTareasPruebasUnitariasTheme {
                PantallaTareas()
            }
        }

        // Validar estado inicial
        composeTestRule.onNodeWithText("Tareas pendientes: 0").assertExists()

        // Agregar una tarea
        composeTestRule.onNodeWithTag("textFieldTarea").performTextInput("Nueva Tarea")
        composeTestRule.onNodeWithTag("botonAgregarTarea").performClick()

        // Validar que se actualiza a 1
        composeTestRule.onNodeWithText("Tareas pendientes: 1").assertExists()
    }

    @Test
    fun campoDeEntrada_aceptaYReflejaTexto() {
        composeTestRule.setContent {
            GestionTareasPruebasUnitariasTheme {
                PantallaTareas()
            }
        }

        val texto = "Prueba de entrada"
        
        composeTestRule.onNodeWithTag("textFieldTarea")
            .performTextInput(texto)
        
        composeTestRule.onNodeWithTag("textFieldTarea")
            .assertTextContains(texto)
    }
}
