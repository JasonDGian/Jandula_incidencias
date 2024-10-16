package ies.jandula.incidencia.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies.jandula.incidencia.dto.IncidenciaCreationDTO;
import ies.jandula.incidencia.dto.IncidenciaDTO;
import ies.jandula.incidencia.entity.IncidenciaEntity;
import ies.jandula.incidencia.mappers.IncidenciaMapper;
import ies.jandula.incidencia.repository.IIncidenciaRepository;
import ies.jandula.incidencia.utils.Constants;
import lombok.extern.slf4j.Slf4j;

@Slf4j // añade el logger.
@RestController
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	// Auto-inyeccion de repositorio.
	private IIncidenciaRepository iIncidenciaRepository;
	
	@Autowired
	IncidenciaMapper incidenciaMapper;

	// =========================== ENDPOINTS ===========================//

	// CREAR INCIDENCIA POR DTO
	// La idea es recibir un cuerpo JSON con los datos de un DTO.
	// Ese dto comprobar que esté correctamente definido.
	// Una vez comprobado que está bien definido entonces crear una nueva entidad
	// incidencia.
	// A partir de esa incidencia guardar una nueva entrada en la base de datos.
	// Gestionar excepciones.
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<String> crearIncidencia(
			@RequestHeader(value = "correo-docente", required = true) String correoDocente,
			@RequestBody(required = true) IncidenciaCreationDTO nuevaIncidenciaDTO)
	{
		try
		{
			// Loguea los parametros recibidos para fines diagnosticos.
			log.debug("Parametros recibidos:\n" + nuevaIncidenciaDTO.toString());

			// Si el numero de aula está vacio o solo espacios.
			if (nuevaIncidenciaDTO.getNumeroAula() == null || nuevaIncidenciaDTO.getNumeroAula().isBlank())
			{
				log.error("Intento de creación de incidencia con numero de aula no definido");
				return ResponseEntity.badRequest().body("ERROR: Numero de aula nulo o vacio.");
			}

			// Si la descripcion está vacia o solo espacios.
			if (nuevaIncidenciaDTO.getDescripcionIncidencia() == null
					|| nuevaIncidenciaDTO.getDescripcionIncidencia().isBlank())
			{
				log.debug("Intento de creación de incidencia con descripcion no definida");
				return ResponseEntity.badRequest().body("ERROR: Descripcion de incidencia nulo o vacio.");
			}

			// Si tanto numero de aula como descripción han sido definidos correctamente
			// creamos nueva incidencia.
			IncidenciaEntity incidencia = new IncidenciaEntity();
			log.debug("DEBUG: Nueva incidencia creada con éxito.");

			// Asignar el numero del aula.
			incidencia.setNumeroAula(nuevaIncidenciaDTO.getNumeroAula());
			log.debug("DEBUG: Numero de Aula asignado con éxito.");

			// Asignar el correo docente al objeto desde el header.
			incidencia.setCorreoDocente(correoDocente);
			log.debug("DEBUG: Correo docente asignado con éxito.");

			// Inicializar el es@RequestMapping(method = RequestMethod.GET)tado de la
			// incidencia by default.
			incidencia.setEstadoIncidencia(Constants.PENDIENTE);
			log.debug("DEBUG: Estado pendiente asignado con éxito.");
			// Inicializa comentario vacio.
			incidencia.setComentario("N/A");
			log.debug("DEBUG: Comentario incidencia inicializado con éxito.");

			// Asigna descripcion.
			incidencia.setDescripcionIncidencia(nuevaIncidenciaDTO.getDescripcionIncidencia());
			log.debug("DEBUG: Descripcion incidencia almacenada con éxito.");

			// Automatiza la asignación de la fecha.
			// Hora española.
			ZonedDateTime currentTimeInSpain = ZonedDateTime.now(ZoneId.of("Europe/Madrid"));
			LocalDateTime localDateTime = currentTimeInSpain.toLocalDateTime();
			incidencia.setFechaIncidencia(localDateTime);
			log.debug("DEBUG: Fecha de creacion almacenada {}", incidencia.getFechaIncidencia());

			log.debug("DEBUG: El objeto incidencia creado es:\n " + incidencia.toString());
			// Finalmente guarda la incidencia en la BBDD.
			iIncidenciaRepository.saveAndFlush(incidencia);

			// Información para registro.
			log.info("INFO: El objeto guardado en base de datos es:\n" + incidencia.toString());

			// Informe a cliente del exito de la operacion.
			return ResponseEntity.status(HttpStatus.CREATED).body("EXITO: Incidencia creada correctamente.");

		} catch (Exception e)
		{
			log.error("Excepción capturada en creaIncidencia: {}", e.getMessage(), e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en metodo crearIncidencia()\nMensaje error:" + e.getMessage());
		}

	}

	@RequestMapping(method = RequestMethod.GET)
	public List<IncidenciaDTO> buscaIncidencia(@RequestParam(required = false) String numeroAula,
			@RequestParam(required = false) String correoDocente,
			@RequestParam(required = false) LocalDateTime fechaIncidencia,
			@RequestParam(required = false) String descripcionIncidencia,
			@RequestParam(required = false) String estadoIncidencia, @RequestParam(required = false) String comentario)
	{

		return iIncidenciaRepository.buscaIncidencia(numeroAula, correoDocente, fechaIncidencia, fechaIncidencia,
				descripcionIncidencia, estadoIncidencia, comentario);

	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> borraIncidencia(@RequestBody(required = true) IncidenciaDTO incidenciaABorrar)
	{			
		
		//TODO: Hacer log, controlar errores, validar inputs. retornar respuestas de exito/fallo/error.
		// si los campos clave no estan nulos
		// crear una nuevo idedntificador incidencia
		// con ese nuevo identificador, borrar de la base de datos.
		
		try
		{
			iIncidenciaRepository.delete( incidenciaMapper.mapToEntity(incidenciaABorrar) );
			

		} catch (Exception e)
		{
			log.error("Excepción capturada en borraIncidencia: {}", e.getMessage(), e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en metodo borraIncidencia()\nMensaje error:" + e.getMessage());
		}

		// Controlar si la incidencia existe
		// si no existe dar mensaje error
		// si la incidencia existe loguear y borrarla
		// retornar mensaje de exito
		// controlar excepciones

		return null;

	}
	


}
