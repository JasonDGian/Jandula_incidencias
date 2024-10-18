package ies.jandula.incidencia.dto;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un filtro de búsqueda para incidencias.
 * 
 * <p>
 * Esta clase se utiliza para encapsular los criterios de búsqueda que se
 * pueden aplicar al consultar incidencias en el sistema. Permite especificar
 * distintos parámetros que se pueden usar para filtrar los resultados de la
 * búsqueda.
 * </p>
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FiltroBusqueda {

    /**
     * Atributo - Número de aula para filtrar las incidencias.
     * 
     * Este atributo permite especificar el aula en la que se registraron
     * las incidencias. Si se establece, se buscarán incidencias relacionadas
     * con este aula en particular.
     */
    private String numeroAula;

    /**
     * Atributo - Correo del docente para filtrar las incidencias.
     * 
     * Este atributo permite especificar el correo del docente que reportó
     * las incidencias. Se utilizará para filtrar las incidencias asociadas
     * a este docente.
     */
    private String correoDocente;

    /**
     * Atributo - Fecha de inicio para filtrar las incidencias.
     * 
     * Este atributo indica la fecha y hora de inicio del rango de búsqueda
     * para las incidencias. Solo se considerarán incidencias registradas a
     * partir de esta fecha y hora.
     */
    private LocalDateTime fechaInicio;

    /**
     * Atributo - Fecha de fin para filtrar las incidencias.
     * 
     * Este atributo indica la fecha y hora de fin del rango de búsqueda
     * para las incidencias. Solo se considerarán incidencias registradas
     * hasta esta fecha y hora.
     */
    private LocalDateTime fechaFin;

    /**
     * Atributo - Descripción de la incidencia para filtrar.
     * 
     * Este atributo permite especificar parte de la descripción de la
     * incidencia que se desea buscar. Se utilizará para filtrar incidencias
     * que contengan el texto proporcionado.
     */
    private String descripcionIncidencia;

    /**
     * Atributo - Estado de la incidencia para filtrar.
     * 
     * Este atributo permite especificar el estado de la incidencia que se
     * desea buscar, como "EN PROGRESO", "CANCELADA", "RESUELTA" o "PENDIENTE".
     */
    private String estadoIncidencia;

    /**
     * Atributo - Comentario relacionado a la incidencia para filtrar.
     * 
     * Este atributo permite especificar parte del comentario asociado a la
     * incidencia que se desea buscar. Se utilizará para filtrar incidencias
     * que contengan el texto proporcionado en sus comentarios.
     */
    private String comentario;
}
