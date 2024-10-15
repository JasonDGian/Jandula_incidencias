package ies.jandula.incidencia.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaEntityId implements Serializable
{
	private static final long serialVersionUID = 1L;

	/**
	 * Atributo Identificativo - Aula en la que se da la incidencia.
	 */
	private String numeroAula;

	/**
	 * Atributo Identificativo - Correo del docente que informa de la incidencia.
	 */
	private String correoDocente;

	/**
	 * Atributo Identificativo - Fecha de creación de la señalación.
	 */
	private LocalDateTime fechaIncidencia;

}
