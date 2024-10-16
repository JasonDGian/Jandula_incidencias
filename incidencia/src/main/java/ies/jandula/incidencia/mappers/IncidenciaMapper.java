package ies.jandula.incidencia.mappers;

import org.springframework.stereotype.Component;

import ies.jandula.incidencia.dto.IncidenciaDTO;
import ies.jandula.incidencia.entity.IncidenciaEntity;

@Component
public class IncidenciaMapper
{
	public IncidenciaEntity mapToEntity( IncidenciaDTO dto ) {
		
		IncidenciaEntity incidencia = new IncidenciaEntity();
		
		incidencia.setNumeroAula(dto.getNumeroAula());
		incidencia.setCorreoDocente(dto.getCorreoDocente());
		incidencia.setFechaIncidencia(dto.getFechaIncidencia());
		incidencia.setDescripcionIncidencia(dto.getDescripcionIncidencia());
		incidencia.setEstadoIncidencia(dto.getEstadoIncidencia());
		incidencia.setComentario(dto.getComentario());
		
		return incidencia;
	}
}

