package ies.jandula.incidencia.mappers;

import org.springframework.stereotype.Component;

import ies.jandula.incidencia.dto.IncidenciaDTO;
import ies.jandula.incidencia.entity.IncidenciaEntity;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class IncidenciaMapper
{

	/**
	 * Mapea un objeto IncidenciaDTO a un objeto IncidenciaEntity.
	 * 
	 * Este método toma un objeto DTO y convierte sus atributos en un objeto entidad.
	 * Primero, valida el DTO para asegurarse de que no sea nulo y de que todos sus 
	 * atributos obligatorios estén presentes. Si el DTO es inválido, lanza una 
	 * IllegalArgumentException. En caso de un error inesperado durante el mapeo, 
	 * lanza una excepción genérica con un mensaje de error.
	 *
	 * @param dto El objeto IncidenciaDTO que se desea mapear a la entidad.
	 * @return Un objeto IncidenciaEntity que contiene los datos del DTO.
	 * @throws Exception Si ocurre un error inesperado durante el mapeo.
	 * @throws IllegalArgumentException Si el objeto DTO es nulo o tiene atributos nulos.
	 */
	public IncidenciaEntity mapToEntity(IncidenciaDTO dto) throws Exception, IllegalArgumentException
	{
		try
		{
			// Si el DTO recibido es NULL o tiene atributos NULL.
			if (!dtoIsValid(dto))
			{
				// Lanza excepcion de argumento ilegal.
				throw new IllegalArgumentException("El DTO recibido es nulo o tiene atributos nulos.");
			}

			// Si el DTO es valido entonces crea nuevo objeto entidad.
			IncidenciaEntity incidencia = new IncidenciaEntity();
			log.debug("DEBUG: Nueva incidencia creada");

			// Mapea los datos del DTO a la nueva entidad.
			incidencia.setNumeroAula(dto.getNumeroAula());
			incidencia.setCorreoDocente(dto.getCorreoDocente());
			incidencia.setFechaIncidencia(dto.getFechaIncidencia());
			incidencia.setDescripcionIncidencia(dto.getDescripcionIncidencia());
			incidencia.setEstadoIncidencia(dto.getEstadoIncidencia());
			incidencia.setComentario(dto.getComentario());
			log.debug("DEBUG: Incidencia mapeada con éxito.\n Incidenecia: {}", incidencia.toString());

			// Retorna el nuevo objeto completamente cargado con los datos correspondientes.
			return incidencia;

		}
		// Bloque de captura de errores no esperados o calculados.
		catch (	Exception e)
		{
			log.error("ERROR: Error inesperado en mapper.", e); // Supuestamente esto devuelve la traza entera.
			throw new Exception("ERROR: El mapeo de objeto ha fallado.\n " + e.getMessage());
		}

	}

	/**
	 * Verifica si el objeto IncidenciaDTO proporcionado es válido.
	 *
	 * Este método comprueba que el objeto DTO no sea nulo y que todos sus atributos
	 * obligatorios (numeroAula, correoDocente, fechaIncidencia, descripcionIncidencia, 
	 * estadoIncidencia y comentario) estén presentes (no sean nulos).
	 * 
	 * @param dto El objeto IncidenciaDTO a validar.
	 * @return true si el DTO es válido (es decir, no es nulo y todos los atributos obligatorios están presentes);
	 *         false si el objeto es nulo o alguno de sus atributos obligatorios es nulo.
	 */
	public boolean dtoIsValid(IncidenciaDTO dto)
	{
		return !(dto == null || dto.getNumeroAula() == null || dto.getCorreoDocente() == null
				|| dto.getFechaIncidencia() == null || dto.getDescripcionIncidencia() == null
				|| dto.getEstadoIncidencia() == null || dto.getComentario() == null);
	}
}
