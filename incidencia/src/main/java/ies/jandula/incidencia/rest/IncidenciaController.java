package ies.jandula.incidencia.rest;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies.jandula.incidencia.models.Incidencia;
import ies.jandula.incidencia.repository.IIncidenciaRepository;
import ies.jandula.incidencia.utils.Constants;
import lombok.extern.slf4j.Slf4j;

/**
 * Controlador REST para la gestión de incidencias en el sistema.
 * 
 * Esta clase maneja las solicitudes HTTP relacionadas con la creación,
 * actualización, búsqueda, eliminación y listado de incidencias. Proporciona
 * endpoints para realizar operaciones como: - Crear una nueva incidencia a
 * partir de los datos enviados en el cuerpo de la solicitud. - Listar todas las
 * incidencias almacenadas en la base de datos. - Buscar una incidencia
 * específica por su ID. - Actualizar el estado de una incidencia y añadir un
 * comentario sobre su solución. - Cargar un lote de incidencias para facilitar
 * pruebas de uso. - Eliminar una incidencia utilizando su ID (eliminar de la
 * bbdd).
 * 
 * Se utiliza inyección de dependencias para acceder al repositorio de
 * incidencias y se implementa un manejo de excepciones para capturar errores y
 * retornar mensajes apropiados. La clase también incluye métodos para filtrar
 * incidencias por estado y modificar su estado a "PENDIENTE", "RESUELTA",
 * "CANCELADA", "EN PROGRESO", entre otros.
 * 
 * Las operaciones se realizan en un contexto transaccional, y se registran las
 * acciones y excepciones en los logs para facilitar la depuración y el
 * mantenimiento del sistema.
 */

@Slf4j // añade el logger.
@RestController
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	// Auto-inyeccion de repositorio.
	private IIncidenciaRepository repo;

	// =========================== ENDPOINTS ===========================//

	/**
	 * CREAR NUEVA INCIDENCIA.
	 * 
	 * Crea una nueva incidencia a partir de los datos proporcionados en el cuerpo
	 * de la solicitud. Verifica que el número de aula y la descripción no estén
	 * vacíos. Si alguno de estos campos es inválido, retorna un error. Si los datos
	 * son correctos, asigna el correo del docente recibido en el header, establece
	 * el estado de la incidencia como "PENDIENTE", y guarda la incidencia en la
	 * base de datos con la fecha actual.
	 * 
	 * @param correoDocente Correo del docente, recibido en el encabezado de la
	 *                      solicitud.
	 * @param incidencia    Objeto {@link Incidencia} con los datos de la incidencia
	 *                      recibidos en el cuerpo de la solicitud.
	 * @return {@link ResponseEntity} con el estado de la operación: éxito (201
	 *         CREATED) o error en caso de fallo.
	 */
	@PostMapping(value = "/nueva") // <host>/incidencias/nueva POST
	public ResponseEntity<String> creaIncidencia(
			@RequestHeader(value = "correo-docente", required = true) String correoDocente,
			@RequestBody Incidencia incidencia)
	{
		try
		{
			log.debug("Parametros recibidos: Correo - {}, Numero - {}, Descripcion - {}", correoDocente,
					incidencia.getNumeroAula(), incidencia.getDescripcionIncidencia());

			// Si el numero de aula no está vacio.
			if (controlaNuloBlanco(incidencia.getNumeroAula()))
			{
				log.debug("Intento de creación de incidencia con numero de aula no definido");
				return ResponseEntity.badRequest().body("ERROR: Numero de aula nulo o vacio.");
			}
			// Si el numero de aula no está vacio.
			if (controlaNuloBlanco(incidencia.getDescripcionIncidencia()))
			{
				log.debug("Intento de creación de incidencia con descripcion no definida");
				return ResponseEntity.badRequest().body("ERROR: Descripcion de incidencia nulo o vacio.");
			}

			// Si tanto numero de aula como descripción han sido definidos correctamente.
			// Asignar el correo docente al objeto.
			incidencia.setCorreoDocente(correoDocente);
			// Inicializar el estado de la incidencia by default.
			incidencia.setEstadoIncidencia(Constants.PENDIENTE);
			incidencia.setComentario("");

			// Automatiza la asignación de la fecha.
			// Hora española.
			ZonedDateTime currentTimeInSpain = ZonedDateTime.now(java.time.ZoneId.of("Europe/Madrid"));
			incidencia.setFechaIncidencia(Date.from(currentTimeInSpain.toInstant()));
			// Finalmente guarda la incidencia en la BBDD.

			repo.saveAndFlush(incidencia);
			log.debug("Objeto guardado en base de datos.\n" + incidencia.toString());

			return ResponseEntity.status(HttpStatus.CREATED).body("EXITO: Incidencia creada correctamente");

		} catch (Exception e)
		{
			log.error("Excepción capturada en creaIncidencia: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}

	}

	/**
	 * LISTAR TODAS LAS INCIDENCIAS.
	 * 
	 * Recupera y devuelve una lista con todas las incidencias almacenadas en la
	 * base de datos. En caso de éxito, retorna un estado HTTP 200 (OK) con la lista
	 * de incidencias. Si ocurre una excepción, se captura y se retorna un error
	 * inesperado.
	 * 
	 * @return {@link ResponseEntity} con la lista de incidencias o un error en caso
	 *         de fallo.
	 */
	@GetMapping // Escucha en <host>/incidencias GET
	public ResponseEntity<?> listarIncidencias()
	{
		try
		{

			return ResponseEntity.ok().body(repo.findAll());
		} catch (Exception e)
		{
			log.error("Excepción capturada en listarIncidencias: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}

	/**
	 * BUSCA INCIDENCIA POR ID
	 * 
	 * Busca una incidencia por su ID en la base de datos. Si la incidencia existe,
	 * retorna un estado HTTP 200 (OK) con el objeto de la incidencia. Si no se
	 * encuentra la incidencia, retorna un mensaje de error indicando que el ID no
	 * fue encontrado. En caso de que ocurra una excepción, se captura y se devuelve
	 * un error inesperado.
	 * 
	 * @param id ID de la incidencia a buscar, recibido como parámetro en la
	 *           solicitud.
	 * @return {@link ResponseEntity} con la incidencia encontrada o un mensaje de
	 *         error si no se encuentra.
	 */
	@PostMapping // <host>/incidencias POST
	public ResponseEntity<?> buscaIncidencia(@RequestParam long id)
	{
		try
		{
			Optional<Incidencia> optIncidencia = repo.findById(id);

			if (optIncidencia.isPresent())
			{
				return ResponseEntity.ok().body(optIncidencia.get());
			} else
			{
				return Constants.ID_NO_ENCONTRADO;
			}
		} catch (Exception e)
		{
			log.error("Excepción capturada en buscaIncidencia: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}

	/**
	 * ACTUALIZA POR ID, CON ESTADO Y COMENTARIO.
	 * 
	 * Actualiza el estado y añade un comentario a una incidencia en la base de
	 * datos. El ID de la incidencia se recibe como un parámetro de la URL, mientras
	 * que el nuevo estado y el comentario se reciben en el cuerpo de la solicitud.
	 * 
	 * Ejemplo parametro: localhost:8888/incidencias/actualiza?id=2 Ejemplo cuerpo:
	 * { "estado":"RESUELTA", "comentario":"Incidencia resuelta" }
	 * 
	 * @param id         - ID de la incidencia a modificar, recibido como parámetro
	 *                   de consulta.
	 * @param estado     - Nuevo estado de la incidencia, recibido en el cuerpo de
	 *                   la solicitud.
	 * @param comentario - Comentario adicional o mensaje, recibido en el cuerpo de
	 *                   la solicitud.
	 * @return {@link ResponseEntity} con un mensaje de éxito o error dependiendo
	 *         del resultado.
	 */
	@PostMapping(value = "/actualiza")
	public ResponseEntity<String> actualizaEstado(@RequestParam(required = true) long id,
			@RequestBody(required = true) String estado, String comentario)
	{
		return modificaEstadoIncidencia(id, estado, comentario);
	}

	/**
	 * CARGA LOTE DE INCIDENCIAS - Para ambito de pruebas.
	 * 
	 * Este método permite cargar un conjunto de incidencias en el sistema de manera
	 * masiva. Se espera que las incidencias sean proporcionadas en formato JSON en
	 * el cuerpo de la solicitud.
	 * 
	 * Por cada incidencia recibida, se crea una nueva entrada en el sistema
	 * llamando al método `creaIncidencia`. En caso de éxito, retorna una respuesta
	 * con el código de estado HTTP 201 (CREATED) indicando que las incidencias
	 * fueron creadas correctamente.
	 * 
	 * Si se produce alguna excepción durante el proceso, se captura y registra en
	 * los logs, y se retorna una respuesta genérica de error inesperado.
	 * 
	 * @param incis Lista de objetos de tipo {@link Incidencia} que representan las
	 *              incidencias a crear.
	 * @return {@link ResponseEntity} con un mensaje de éxito o un error en caso de
	 *         fallo.
	 */
	@PostMapping(value = "/carga-lote")
	public ResponseEntity<String> cargaLote(@RequestBody List<Incidencia> incis)
	{
		try
		{
			int cargadas = 0;
			for (Incidencia inci : incis)
			{
				this.creaIncidencia(inci.getCorreoDocente(), inci);
				cargadas++;
			}
			return ResponseEntity.status(HttpStatus.CREATED).body("Incidencias cargadas: " + cargadas);
		} catch (Exception e)
		{
			log.error("Excepción capturada cargando ficheros demostrativos: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}

	/**
	 * BORRAR INCIDENCIA POR ID.
	 * 
	 * Elimina una incidencia del sistema basada en su ID.
	 * <p>
	 * Este método recibe una solicitud HTTP POST en la ruta "/borrar" con un
	 * parámetro obligatorio que representa el ID de la incidencia a eliminar.
	 * Primero, se busca la incidencia en el repositorio. Si la incidencia existe,
	 * se elimina y se registra un mensaje en los logs con la información del
	 * borrado. Si la incidencia no es encontrada, devuelve una respuesta
	 * predefinida indicando que el ID no fue localizado. En caso de cualquier
	 * excepción, se captura y se devuelve una respuesta de error inesperado.
	 * </p>
	 * 
	 * @param id El ID de la incidencia que se desea eliminar. Es un parámetro
	 *           requerido.
	 * @return ResponseEntity con el estado HTTP correspondiente:
	 *         <ul>
	 *         <li>200 OK: Si la incidencia fue eliminada exitosamente.</li>
	 *         <li>404 NOT FOUND: Si la incidencia no fue encontrada (definido en
	 *         Constants.ID_NO_ENCONTRADO).</li>
	 *         <li>500 INTERNAL SERVER ERROR: Si ocurre algún error inesperado
	 *         (definido en Constants.ERROR_INESPERADO).</li>
	 *         </ul>
	 */
	@DeleteMapping(value = "/borrar")
	public ResponseEntity<String> borrarIncidencia(@RequestParam(required = true) Long id)
	{
		try
		{
			// Busca incidencia.
			Optional<Incidencia> incidenciaOpt = repo.findById(id);

			// Si la incidencia existe.
			if (incidenciaOpt.isPresent())
			{
				// Loguea su borrado.
				log.info("Incidencia con id {} ha sido eliminada en fecha {}", id, LocalDate.now());
				// Borrala.
				repo.deleteById(id);
				// Responde exito.
				return ResponseEntity.status(HttpStatus.OK)
						.body("Incidencia con id " + id + " ha sido eliminada con exito. ");
			} else
			{
				return Constants.ID_NO_ENCONTRADO;
			}

		} catch (Exception e)
		{
			log.error("Excepción capturada cargando ficheros demostrativos: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}
	
	

	// =========================== OTRA LOGICA ===========================//

	/**
	 * MMODIFICA ESTADO INCIDENCIA SIN COMENTARIO.
	 * 
	 * Modifica el estado de una incidencia, omitiendo el comentario.
	 * 
	 * Este método es una sobrecarga que llama a la versión de
	 * modificaEstadoIncidencia pasando null como argumento para el comentario de
	 * solución. Permite actualizar el estado de la incidencia sin necesidad de
	 * proporcionar un comentario.
	 *
	 * @param id     El identificador de la incidencia a modificar.
	 * @param estado El nuevo estado que se asignará a la incidencia.
	 * @return ResponseEntity<String> que indica el resultado de la operación.
	 */
	private ResponseEntity<String> modificaEstadoIncidencia(long id, String estado)
	{
		// invoca la version con comentario puesto a nulo, que obviará ese paso.
		return modificaEstadoIncidencia(id, estado, null);
	}
	
	

	/**
	 * MODIFICA ESTADO INCIDENCIA CON COMENTARIO.
	 * 
	 * Modifica el estado de una incidencia y añade un comentario sobre su solución.
	 * 
	 * Este método busca la incidencia por su ID, verifica si ya tiene el estado
	 * solicitado y, si no, actualiza el estado y el comentario. Devuelve un
	 * ResponseEntity con un mensaje que indica el resultado de la operación. En
	 * caso de error o si la incidencia no se encuentra, se maneja la excepción y se
	 * devuelve un mensaje apropiado.
	 *
	 * @param id                 El identificador de la incidencia a modificar.
	 * @param estado             El nuevo estado que se asignará a la incidencia.
	 * @param comentarioSolucion El comentario relacionado con la solución de la
	 *                           incidencia.
	 * @return ResponseEntity<String> que indica el resultado de la operación.
	 */
	private ResponseEntity<String> modificaEstadoIncidencia(long id, String estado, String comentarioSolucion)
	{
		try
		{
			Optional<Incidencia> optIncidencia = repo.findById(id);

			if (optIncidencia.isPresent())
			{
				Incidencia incidencia = optIncidencia.get();

				if (incidencia.getEstadoIncidencia().equals(estado))
				{
					return ResponseEntity.ok().body("AVISO: La incidencia ya esta configurada como " + estado + ".");
				} else
				{
					incidencia.setEstadoIncidencia(estado);

					// Si el comentario no es nulo ni blanco, actualiza comentario.
					if (!controlaNuloBlanco(comentarioSolucion))
					{
						incidencia.setComentario(comentarioSolucion);
					}

					log.debug("Objeto guardado en base de datos:\n" + incidencia.toString());

					repo.saveAndFlush(incidencia);
					// llamar a metodo de aviso a profesor (mandar correo de aviso).
					return ResponseEntity.ok()
							.body("EXITO: Incidencia modificada a " + estado + " con éxito.\nComentario agregado.");
				}
			} else
			{
				log.debug(Constants.ID_NO_ENCONTRADO.toString());
				return Constants.ID_NO_ENCONTRADO;
			}
		} catch (Exception e)
		{
			log.error("Excepción capturada en modificaEstadoIncidenciaComentario: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}
	
	

	/**
	 * METODO QOL - Control de cadenas.
	 * 
	 * Verifica si una cadena es nula o está vacía.
	 * 
	 * Este método comprueba si el parámetro 'cadena' es nulo o si no contiene
	 * caracteres (es decir, está en blanco). Devuelve true si la cadena es nula o
	 * está vacía; de lo contrario, devuelve false.
	 *
	 * @param cadena La cadena a verificar.
	 * @return boolean true si la cadena es nula o está en blanco, false en caso
	 *         contrario.
	 */
	private boolean controlaNuloBlanco(String cadena)
	{
		return cadena == null || cadena.isBlank();
	}
	
	

	// =========================== DESCARTABLES? ===========================//

	/**
	 * Filtra las incidencias según el estado proporcionado.
	 * 
	 * Este método maneja las solicitudes POST en la ruta "/filtro". Recibe un
	 * parámetro de consulta 'estado' y busca todas las incidencias que coinciden
	 * con ese estado, devolviéndolas en orden ascendente. En caso de que ocurra una
	 * excepción durante el proceso, se registra el error y se devuelve un mensaje
	 * de error inesperado.
	 *
	 * @param estado El estado de las incidencias que se desea filtrar.
	 * @return ResponseEntity<?> que contiene la lista de incidencias filtradas o un
	 *         mensaje de error en caso de excepción.
	 */
	@PostMapping(value = "/filtro") // <localhost>/incidencias/filtro POST
	public ResponseEntity<?> filtrarIncidenciaSQL(@RequestParam String estado)
	{
		try
		{
			return ResponseEntity.ok().body(repo.findByEstadoIncidenciaOrderAsc(estado));
		} catch (Exception e)
		{
			log.error("Excepción capturada en filtrarIncidenciaSQL: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}

	}
	
	

	// Cambiar a 'PENDIENTE' la incidencia con el ID proporcionado.
	@PostMapping(value = "/reinicia") // <localhost>/incidencias/resuelve POST
	public ResponseEntity<String> reiniciaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.PENDIENTE);
	}

	// Cambia a 'RESUELTA' la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelve") // <localhost>/incidencias/resuelve POST
	public ResponseEntity<String> resuelveIncidencia(@RequestParam(required = true) long id,
			@RequestBody String comentario)
	{
		return modificaEstadoIncidencia(id, Constants.RESUELTA, comentario);
	}

	/**
	 * Cambia a 'CANCELADA' la incidencia con el ID proporcionado.
	 *
	 * Este método maneja las solicitudes POST en la ruta "/cancelar". Recibe un
	 * parámetro de consulta 'id' que corresponde al identificador de la incidencia
	 * a cancelar. Llama al método 'modificaEstadoIncidencia' para actualizar el
	 * estado de la incidencia a "cancelada".
	 *
	 * @param id El identificador de la incidencia a cancelar.
	 * @return ResponseEntity<String> que indica el resultado de la operación de
	 *         cancelación.
	 */
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancela POST
	public ResponseEntity<String> cancelaIncidencia(@RequestParam(required = true) long id,
			@RequestBody String comentario)
	{
		return modificaEstadoIncidencia(id, Constants.CANCELADA, comentario);
	}

	/**
	 * Cambia a 'EN PROGRESO' la incidencia con el ID proporcionado.
	 *
	 * Este método maneja las solicitudes POST en la ruta "/en-progreso". Recibe un
	 * parámetro de consulta 'id' que corresponde al identificador de la incidencia
	 * a activar. Llama al método 'modificaEstadoIncidencia' para actualizar el
	 * estado de la incidencia a "en progreso".
	 *
	 * @param id El identificador de la incidencia a activar.
	 * @return ResponseEntity<String> que indica el resultado de la operación de
	 *         activación.
	 */
	@PostMapping(value = "/en-progreso") // <localhost>/incidencias/cancela POST
	public ResponseEntity<String> activaIncidencia(@RequestParam(required = true) long id,
			@RequestBody String comentario)
	{
		return modificaEstadoIncidencia(id, Constants.EN_PROGRESO, comentario);
	}

}
