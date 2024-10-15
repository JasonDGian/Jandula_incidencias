package ies.jandula.incidencia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaDTO 
{
	/**
	 * Atribtuo - Aula en la que se da la incidencia.
	 */
	private String numeroAula;

	/**
	 * Atribtuo - Correo del docente que informa de la incidencia.
	 */
	private String correoDocente;

	/**
	 * Atribtuo - Fecha de creación de la señalación.
	 */
	private LocalDateTime fechaIncidencia;

	/**
	 * Atribtuo - Detalla el problema relacionado a la incidencia.
	 */
	private String descripcionIncidencia;

	/**
	 * Atribtuo - Define el estado de la incidencia. 
	 */
	private String estadoIncidencia;
	
	/**
	 * Atribtuo - Comentario relacionado a la solucion de la incidencia.
	 */
	private String comentario;

}
