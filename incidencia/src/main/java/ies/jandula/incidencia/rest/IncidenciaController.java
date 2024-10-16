package ies.jandula.incidencia.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
			@RequestBody(required = true) IncidenciaDTO nuevaIncidenciaDTO)
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
				log.error("Intento de creación de incidencia con descripcion no definida");
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

			log.debug("DEBUG: Objeto creado:\n " + incidencia.toString());
			// Finalmente guarda la incidencia en la BBDD.
			iIncidenciaRepository.saveAndFlush(incidencia);

			// Información para registro.
			log.info("INFO: El objeto guardado en base de datos es:\n" + incidencia.toString());

			// Informe a cliente del exito de la operacion.
			return ResponseEntity.status(HttpStatus.CREATED).body("EXITO: Incidencia creada correctamente.");

		} catch (Exception e)
		{
			log.error("Excepción capturada en crearIncidencia(): {}", e.getMessage(), e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en la creación de incidencia.\nMensaje error:" + e.getMessage());
		}

	}
	
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<String> actualizarIncidencia(
			@RequestHeader(value = "correo-docente", required = true) String correoDocente,
			@RequestBody(required = true) IncidenciaDTO nuevaIncidenciaDTO){
		
		try
		{
			
			// Aqui se recibe un objeto DTO con parametros modificados. 
			// Esos parametros se actualizan con el saveandflush.
			// El unico parametro que no se modifica es la fecha de creacion.
			
			// Loguea los parametros recibidos para fines diagnosticos.
			log.debug("Parametros recibidos:\n" + nuevaIncidenciaDTO.toString());

			if (!(incidenciaMapper.dtoIsValid(nuevaIncidenciaDTO))) {
				log.error("ERROR: Objeto DTO invalido o nulo:\n" + nuevaIncidenciaDTO.toString());
				throw new IllegalArgumentException( "Objeto incidencia recibido no valido." );
			}


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

			log.debug("DEBUG: Objeto creado:\n " + incidencia.toString());
			// Finalmente guarda la incidencia en la BBDD.
			iIncidenciaRepository.saveAndFlush(incidencia);

			// Información para registro.
			log.info("INFO: El objeto guardado en base de datos es:\n" + incidencia.toString());

			// Informe a cliente del exito de la operacion.
			return ResponseEntity.status(HttpStatus.CREATED).body("EXITO: Incidencia creada correctamente.");

		} catch (Exception e)
		{
			log.error("Excepción capturada en crearIncidencia(): {}", e.getMessage(), e);

			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en la creación de incidencia.\nMensaje error:" + e.getMessage());
		}
		
	}

	// Metodo de busqueda.
	@RequestMapping(method = RequestMethod.GET)
	public List<IncidenciaDTO> buscaIncidencia(@RequestParam(required = false) String numeroAula,
			@RequestParam(required = false) String correoDocente,
			@RequestParam(required = false) LocalDateTime fechaInicio,
			@RequestParam(required = false) LocalDateTime fechaFin,
			@RequestParam(required = false) String descripcionIncidencia,
			@RequestParam(required = false) String estadoIncidencia, @RequestParam(required = false) String comentario)
	{
		try
		{
			return iIncidenciaRepository.buscaIncidencia(numeroAula, correoDocente, fechaInicio, fechaFin,
					descripcionIncidencia, estadoIncidencia, comentario);
		} catch (Exception e)
		{
			log.error("ERROR: Capturado en buscaIncidencia()\n {}", e);
			return new ArrayList<>();
		}
	}

	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> borraIncidencia(@RequestBody(required = true) IncidenciaDTO dto)
	{
		try

		{
			// El metodo de mapeo tiene metodos de control de objeto y propiedades NULL.
			IncidenciaEntity inEntity = incidenciaMapper.mapToEntity(dto);

			// Controla si la incidencia que se desea borrar existe, si no existe informa en respuesta HTTP.
			if (!(iIncidenciaRepository.existsByNumeroAulaAndCorreoDocenteAndFechaIncidencia(inEntity.getNumeroAula(),
					inEntity.getCorreoDocente(), inEntity.getFechaIncidencia())))
			{
				// En caso de que no exista la incidencia buscada.
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada.");
			}

			iIncidenciaRepository.delete(inEntity);
			log.info("INFO: Incidencia eliminada.\n{}", inEntity.toString());
			
			// Respuesta HTTP de objeto borrado con exito.
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("INFO:Incidencia eliminada con exito.");

		}
		// Error en parametros DTO.
		catch (IllegalArgumentException e)
		{
			log.error("ERROR: Error en parametros del objeto recibido en borraIncidencia().\n{}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
		// Captura de errores no esperados o calculados.
		catch (Exception e)
		{
			log.error("ERROR: Error inesperado capturado en borraIncidencia().\n{}", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
