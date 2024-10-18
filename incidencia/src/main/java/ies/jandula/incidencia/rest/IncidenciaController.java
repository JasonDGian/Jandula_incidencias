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

import ies.jandula.incidencia.dto.FiltroBusqueda;
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

	/**
	 * Crea una nueva incidencia en el sistema.
	 * 
	 * Este método recibe un DTO que contiene la información de la incidencia a
	 * crear y un encabezado que incluye el correo del docente. Se realiza la
	 * validación de los parámetros recibidos y, si son válidos, se crea una nueva
	 * incidencia en la base de datos. Si los datos no son válidos, se devuelve un
	 * código de estado HTTP 400 (Bad Request). En caso de un error inesperado, se
	 * devuelve un código de estado HTTP 500 (Internal Server Error).
	 * 
	 * @param correoDocente      El correo electrónico del docente, que se espera en
	 *                           el encabezado de la solicitud. Este parámetro es
	 *                           requerido y no puede ser nulo.
	 * @param nuevaIncidenciaDTO El objeto DTO que contiene la información de la
	 *                           incidencia a crear. Este parámetro es requerido y
	 *                           no puede ser nulo.
	 * @return ResponseEntity<String> La respuesta que indica el resultado de la
	 *         operación. Si la creación es exitosa, se devuelve un código de estado
	 *         201 (Created) junto con un mensaje de éxito. Si hay un error de
	 *         validación, se devuelve un código de estado 400 (Bad Request) con un
	 *         mensaje de error. Si ocurre un error inesperado, se devuelve un
	 *         código de estado 500 (Internal Server Error) con un mensaje de error.
	 */
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

			// Bloque de asignación de valores a objeto incidencia.
			incidencia.setNumeroAula(nuevaIncidenciaDTO.getNumeroAula());

			incidencia.setCorreoDocente(correoDocente);

			incidencia.setEstadoIncidencia(Constants.PENDIENTE);

			incidencia.setComentario("");

			incidencia.setDescripcionIncidencia(nuevaIncidenciaDTO.getDescripcionIncidencia());

			// Hora local española.
			ZonedDateTime currentTimeInSpain = ZonedDateTime.now(ZoneId.of("Europe/Madrid"));
			LocalDateTime localDateTime = currentTimeInSpain.toLocalDateTime();

			incidencia.setFechaIncidencia(localDateTime);

			log.debug("DEBUG: Objeto incidencia inicializado correctamente:\n " + incidencia.toString());

			// Finalmente guarda la incidencia en la BBDD.
			iIncidenciaRepository.saveAndFlush(incidencia);

			// Información para registro.
			log.info("INFO: El objeto guardado en base de datos es:\n" + incidencia.toString());

			// Informe a cliente del exito de la operacion.
			return ResponseEntity.status(HttpStatus.CREATED).body("EXITO: Nueva incidencia creada con éxito.");

		} catch (Exception e)
		{
			log.error("Excepción capturada en crearIncidencia(): {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en la creación de incidencia.\nMensaje error:" + e.getMessage());
		}

	}

	/**
	 * Actualiza una incidencia existente en la base de datos basándose en los
	 * detalles proporcionados en el DTO. Verifica primero si la incidencia existe.
	 * Si no se encuentra, retorna un código de estado 404 (NOT_FOUND). Si se
	 * encuentra, actualiza los datos y responde con un código 200 (OK).
	 *
	 * @param correoDocente      El correo del docente asociado a la incidencia,
	 *                           extraído de la cabecera HTTP.
	 * @param nuevaIncidenciaDTO El objeto {@link IncidenciaDTO} que contiene los
	 *                           detalles actualizados de la incidencia. Los campos
	 *                           clave (como número de aula, correo del docente y
	 *                           fecha de incidencia) son obligatorios.
	 * @return {@link ResponseEntity} con el código de estado correspondiente: - 200
	 *         (OK) si la incidencia fue actualizada correctamente. - 404
	 *         (NOT_FOUND) si la incidencia no fue encontrada en la base de datos. -
	 *         400 (BAD_REQUEST) si los parámetros del DTO no son válidos. - 500
	 *         (INTERNAL_SERVER_ERROR) en caso de errores inesperados.
	 * @throws IllegalArgumentException si los parámetros del DTO son inválidos o
	 *                                  nulos.
	 */
	@RequestMapping(method = RequestMethod.PUT)
	public ResponseEntity<String> actualizarIncidencia(
			@RequestHeader(value = "correo-docente", required = true) String correoDocente,
			@RequestBody(required = true) IncidenciaDTO nuevaIncidenciaDTO)
	{
		try
		{
			// Loguea los parametros recibidos para fines diagnosticos.
			log.debug("Parametros recibidos:\n" + nuevaIncidenciaDTO.toString());

			// Control de validez de objeto DTO recibido y mapeado a entidad incidencia.
			// lanza IllegalArgumentException
			IncidenciaEntity incidencia = incidenciaMapper.mapToEntity(nuevaIncidenciaDTO);
			log.debug("DEBUG: Nueva incidencia mapeada desde DTO con éxito.\n " + incidencia.toString());

			// Si no existen objetos con ese ID responde 404
			if (!(iIncidenciaRepository.existsByCompositeId(incidencia.getNumeroAula(), incidencia.getCorreoDocente(),
					incidencia.getFechaIncidencia())))
			{
				// En caso de que no exista la incidencia buscada.
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada.");
			}

			// Finalmente guarda la incidencia en la BBDD.
			iIncidenciaRepository.saveAndFlush(incidencia);

			// Información para registro.
			log.info("INFO: Objeto actualizado:\n" + incidencia.toString());

			// Informe a cliente del exito de la operacion.
			return ResponseEntity.status(HttpStatus.OK).body("EXITO: Incidencia modificada con exito.");

		} catch (IllegalArgumentException e)
		{
			log.error("ERROR: Error en parametros del objeto recibido en actualizarIncidencia().\n{}", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		} catch (Exception e)
		{
			log.error("Excepción capturada en actualizarIncidencia(): {}", e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error en la actualizacion de incidencia.\n " + e.getMessage());
		}

	}

	// Metodo de busqueda.
	@RequestMapping(method = RequestMethod.GET)
	public List<IncidenciaDTO> buscaIncidencia(@RequestBody FiltroBusqueda f)
	{
		try
		{
			// Loguea los parametros recibidos
			log.debug("DEBUG: Parametros de busqueda recibidos:\n {}", f.toString());
			
			// Controlar si cuerpo es nulo y responder acorte.
			// Controllar si la lista de resultados es nula y responder con un mensaje HTTP acorde.
			

			// Invoca el metodo con query personalizada para busqueda con nulos.
			List<IncidenciaDTO> listado = iIncidenciaRepository.buscaIncidencia(f.getNumeroAula(), f.getCorreoDocente(),
					f.getFechaInicio(), f.getFechaFin(), f.getDescripcionIncidencia(), f.getEstadoIncidencia(),
					f.getComentario());

			log.debug("DEBUG: Objetos encontrados {}", listado.size());

			return listado;

		} catch (Exception e)
		{
			log.error("ERROR: Capturado en buscaIncidencia()\n {}", e);
			return new ArrayList<>();
		}
	}

	/**
	 * Elimina una incidencia de la base de datos basándose en los detalles
	 * proporcionados en el DTO. Verifica primero si la incidencia existe, y si no,
	 * retorna un código de estado 404 (NOT_FOUND). Si la incidencia existe, la
	 * elimina y retorna un código de estado 204 (NO_CONTENT) indicando éxito.
	 *
	 * @param dto El objeto {@link IncidenciaDTO} que contiene los detalles de la
	 *            incidencia a eliminar. Los campos {@code numeroAula},
	 *            {@code correoDocente} y {@code fechaIncidencia} son obligatorios.
	 * @return {@link ResponseEntity} con el código de estado correspondiente: - 204
	 *         (NO_CONTENT) si la incidencia fue eliminada correctamente. - 404
	 *         (NOT_FOUND) si la incidencia no fue encontrada en la base de datos. -
	 *         400 (BAD_REQUEST) si los parámetros del DTO no son válidos. - 500
	 *         (INTERNAL_SERVER_ERROR) en caso de errores inesperados.
	 * @throws IllegalArgumentException si los parámetros del DTO son inválidos.
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	public ResponseEntity<String> borraIncidencia(@RequestBody(required = true) IncidenciaDTO dto)
	{
		try
		{
			// Mapea el DTO recibido a la entidad de Incidencia y controla parametros NULL.
			IncidenciaEntity inEntity = incidenciaMapper.mapToEntity(dto);

			// Verifica si la incidencia existe en la base de datos.
			if (!(iIncidenciaRepository.existsByCompositeId(inEntity.getNumeroAula(), inEntity.getCorreoDocente(),
					inEntity.getFechaIncidencia())))
			{
				// Si no existe la incidencia, responde con 404.
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Incidencia no encontrada.");
			}

			// Elimina la incidencia de la base de datos y loguea la accion.
			iIncidenciaRepository.delete(inEntity);
			log.info("INFO: Incidencia eliminada con exito.\n{}", inEntity.toString());

			// Respuesta HTTP de objeto borrado con exito.
			return ResponseEntity.status(HttpStatus.NO_CONTENT).body("INFO:Incidencia eliminada con exito.");

		}
		// Error en parametros DTO u objeto nulo.
		catch (IllegalArgumentException e)
		{
			log.error("ERROR: Error en parametros del objeto recibido en borraIncidencia().\n{}", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
		// Captura de errores no esperados o calculados.
		catch (Exception e)
		{
			log.error("Error inesperado en borraIncidencia() .\nMensaje de error: ", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
		}
	}

}
