package ies.jandula.incidencia.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IncidenciaCreationDTO
{
	/**
	 * Atribtuo - Aula en la que se da la incidencia.
	 */
	private String numeroAula;

	/**
	 * Atribtuo - Detalla el problema relacionado a la incidencia.
	 */
	private String descripcionIncidencia;

}
