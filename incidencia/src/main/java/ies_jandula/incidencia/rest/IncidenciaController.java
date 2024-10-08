package ies_jandula.incidencia.rest;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.HeadersBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies_jandula.incidencia.models.Incidencia;
import ies_jandula.incidencia.repository.IIncidenciaRepository;
import ies_jandula.incidencia.utils.Constants;

@RestController
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	IIncidenciaRepository repo;

	/**
	 * Atiende a peticiones GETR en localhost:8888/incidencias 
	 * 
	 * Si la operación es exitosa, devuelve una respuesta con un estado HTTP 200 OK
	 * y un cuerpo que contiene una lista de objetos de tipo `Incidencia`.
	 * 
	 * En caso de que ocurra un error durante la recuperación de las incidencias, se
	 * captura la excepción y se devuelve una respuesta con un estado HTTP 500
	 * Internal Server Error, sin contenido en el cuerpo de la respuesta.
	 * 
	 * @return ResponseEntity<List<Incidencia>> - Un objeto ResponseEntity 
	 *         que contiene la lista de incidencias y el estado de la respuesta.
	 */
	@GetMapping // <localhost>/incidencias GET
	public ResponseEntity<List<Incidencia>> listarIncidencias()
	{
		try
		{
			return ResponseEntity.ok().body(repo.findAll());
		} catch (Exception e)
		{
			return ResponseEntity.internalServerError().build();
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
				return ResponseEntity.badRequest().body("No existen incidencias con esta ID");
			}
		} catch (Exception e)
		{
			return ResponseEntity.internalServerError().body("Se ha producido un error");
		}
	}

	// Generar una nueva incidencia.
	@PostMapping(value = "/nueva") // <localhost>/incidencias/nueva POST
	public String creaIncidencia(@RequestHeader(value = "correo-docente", required = true) String correoDocente,
			@RequestBody Incidencia incidencia)
	{
		/*
		 * HACER BLOQUE DE VALIDACIÓN DE OBJETO JSON INCIDENCIA.
		 * 
		 * EN LA CABECERA: El unico campo necesario es el correo del docente.
		 * 
		 * EN EL CUERPO: Los unicos campos necesarios son "numero de aula" y
		 * "descripcion".
		 */

		// Asignar el correo al objeto.
		incidencia.setCorreoDocente(correoDocente);
		// Inicializar el estado de la incidencia by default.
		incidencia.setEstadoIncidencia(Constants.PENDIENTE);

		// Automatiza la asignación de la fecha.
		// Hora española.
		ZonedDateTime currentTimeInSpain = ZonedDateTime.now(java.time.ZoneId.of("Europe/Madrid"));
		incidencia.setFechaIncidencia(Date.from(currentTimeInSpain.toInstant()));

		repo.saveAndFlush(incidencia);

		return "Incidencia creada " + incidencia.toString();
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/reinicia") // <localhost>/incidencias/resuelve POST
	public ResponseEntity<String> reiniciaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.PENDIENTE);
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelve") // <localhost>/incidencias/resuelve POST
	public ResponseEntity<String> resuelveIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.RESUELTA);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancela POST
	public ResponseEntity<String> cancelaIncidencia(@RequestParam(required = true) long id)
	{

		return modificaEstadoIncidencia(id, Constants.CANCELADA);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/en-progreso") // <localhost>/incidencias/cancela POST
	public ResponseEntity<String> activaIncidencia(@RequestParam(required = true) long id)
	{

		return modificaEstadoIncidencia(id, Constants.EN_PROGRESO);

	}

	@PostMapping(value = "/filtro") // <localhost>/incidencias/filtro2 POST
	public List<Incidencia> filtrarIncidenciaSQL(@RequestParam String estado)
	{
		// Devuelve listado ordenado
		return repo.findByEstadoIncidenciaOrderAsc(estado);
	}

	/**
	 * Este método permite modificar el estado de una incidencia identificada por su
	 * ID. Realiza las siguientes validaciones y acciones:
	 * 
	 * 1. Busca la incidencia en el repositorio usando el ID proporcionado. - Si no
	 * se encuentra una incidencia con ese ID, devuelve un mensaje de error
	 * informando que no existe.
	 * 
	 * 2. Si se encuentra la incidencia: - Verifica si ya tiene el estado que se
	 * desea aplicar. - Si ya está en el mismo estado, devuelve un mensaje de aviso
	 * indicando que no es necesario actualizarla. - Si la incidencia tiene un
	 * estado diferente: - Actualiza el estado de la incidencia con el nuevo valor.
	 * - Guarda los cambios en el repositorio. - (Posible envío de aviso al
	 * profesor, pero este código no está implementado aquí). - Devuelve un mensaje
	 * de éxito indicando que el estado ha sido modificado correctamente.
	 * 
	 * @param id     - El identificador único de la incidencia a modificar.
	 * @param estado - El nuevo estado que se desea aplicar a la incidencia.
	 * @return Un mensaje indicando el resultado de la operación: - "ERROR: No
	 *         existen incidencias con ese ID." si la incidencia no se encuentra. -
	 *         "AVISO: La incidencia ya estaba configurada como [estado]." si el
	 *         estado ya es el solicitado. - "EXITO: Incidencia modificada a
	 *         [estado] con éxito." si la actualización fue exitosa.
	 */
	private ResponseEntity<String> modificaEstadoIncidencia(long id, String estado)
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
					repo.saveAndFlush(incidencia);
					// llamar a metodo de aviso a profesor (mandar correo de aviso).
					return ResponseEntity.ok().body("EXITO: Incidencia modificada a " + estado + " con éxito.");
				}
			} else
			{
				return ResponseEntity.badRequest().body("ERROR: No existen incidencias con ese ID.");
			}
		} catch (Exception e)
		{

			return ResponseEntity.internalServerError().body("Se ha producido un error.");
		}
	}
}
