package ies.jandula.incidencia.rest;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
 * Clase Controller que atiende a las peticiones relacionadas al manejo de
 * incidencias.
 */
@Slf4j // añade el logger.
@RestController
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	private IIncidenciaRepository repo;

	@GetMapping // Escucha en localhost:8888/incidencias
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

	@PostMapping // <localhost>:8888/incidencias POST
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

	// Generar una nueva incidencia.
	@PostMapping(value = "/nueva") // <localhost>/incidencias/nueva POST
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
			incidencia.setComentarioSolucion("");
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
	
	// hacer un endpoint que recibe como queryparam un ID.
	// ademas recibe un cvuerpo con ESTADO y MENSAJE.
	// Endpoint se llama Modificar con mensaje.
	
	

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/reinicia") // <localhost>/incidencias/resuelve POST
	private ResponseEntity<String> reiniciaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.PENDIENTE);
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelve") // <localhost>/incidencias/resuelve POST
	private ResponseEntity<String> resuelveIncidencia(@RequestParam(required = true) long id,
			@RequestBody(required = true) String comentarioSolucion)
	{
		// Añadir validación y bloque de control.
		// aquí la cadena comentarioSolucion es un texto plano, cambiar a Json?
		return modificaEstadoIncidencia(id, Constants.RESUELTA, comentarioSolucion);
	}

	/**
	 * Cancela una incidencia estableciendo su estado como "cancelada".
	 *
	 * Este método maneja las solicitudes POST en la ruta "/cancelar". 
	 * Recibe un parámetro de consulta 'id' que corresponde al 
	 * identificador de la incidencia a cancelar. Llama al método 
	 * 'modificaEstadoIncidencia' para actualizar el estado de 
	 * la incidencia a "cancelada".
	 *
	 * @param id El identificador de la incidencia a cancelar.
	 * @return ResponseEntity<String> que indica el resultado de la 
	 *         operación de cancelación.
	 */
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancela POST
	private ResponseEntity<String> cancelaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.CANCELADA);
	}

	/**
	 * Activa una incidencia estableciendo su estado como "en progreso".
	 *
	 * Este método maneja las solicitudes POST en la ruta "/en-progreso". 
	 * Recibe un parámetro de consulta 'id' que corresponde al 
	 * identificador de la incidencia a activar. Llama al método 
	 * 'modificaEstadoIncidencia' para actualizar el estado de 
	 * la incidencia a "en progreso".
	 *
	 * @param id El identificador de la incidencia a activar.
	 * @return ResponseEntity<String> que indica el resultado de la 
	 *         operación de activación.
	 */
	@PostMapping(value = "/en-progreso") // <localhost>/incidencias/cancela POST
	private ResponseEntity<String> activaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.EN_PROGRESO);
	}

	/**
	 * Filtra las incidencias según el estado proporcionado.
	 * 
	 * Este método maneja las solicitudes POST en la ruta "/filtro". 
	 * Recibe un parámetro de consulta 'estado' y busca todas las 
	 * incidencias que coinciden con ese estado, devolviéndolas en 
	 * orden ascendente. En caso de que ocurra una excepción durante 
	 * el proceso, se registra el error y se devuelve un mensaje 
	 * de error inesperado.
	 *
	 * @param estado El estado de las incidencias que se desea filtrar.
	 * @return ResponseEntity<?> que contiene la lista de incidencias filtradas 
	 *         o un mensaje de error en caso de excepción.
	 */
	@PostMapping(value = "/filtro") // <localhost>/incidencias/filtro POST
	private ResponseEntity<?> filtrarIncidenciaSQL(@RequestParam String estado)
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
	
	/**
	 * Modifica el estado de una incidencia, omitiendo el comentario.
	 * 
	 * Este método es una sobrecarga que llama a la versión de 
	 * modificaEstadoIncidencia pasando null como argumento para 
	 * el comentario de solución. Permite actualizar el estado 
	 * de la incidencia sin necesidad de proporcionar un comentario.
	 *
	 * @param id El identificador de la incidencia a modificar.
	 * @param estado El nuevo estado que se asignará a la incidencia.
	 * @return ResponseEntity<String> que indica el resultado de la operación.
	 */
	private ResponseEntity<String> modificaEstadoIncidencia(long id, String estado){
		// invoca la version con comentario puesto a nulo, que obviará ese paso.
		return modificaEstadoIncidencia(id, estado, null);
	}

	/**
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
						incidencia.setComentarioSolucion(comentarioSolucion);
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
	 * METODO QOL
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

}
