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

	@GetMapping
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
			log.debug( "Parametros recibidos: Correo - {}, Numero - {}, Descripcion - {}", correoDocente, incidencia.getNumeroAula(), incidencia.getDescripcionIncidencia()  );
			
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

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/reinicia") // <localhost>/incidencias/resuelve POST
	private ResponseEntity<String> reiniciaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.PENDIENTE);
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelve") // <localhost>/incidencias/resuelve POST
	private ResponseEntity<String> resuelveIncidencia(@RequestParam(required = true) long id, String comentarioSolucion)
	{

		return modificaEstadoIncidencia(id, Constants.RESUELTA, comentarioSolucion);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancela POST
	private ResponseEntity<String> cancelaIncidencia(@RequestParam(required = true) long id)
	{

		return modificaEstadoIncidencia(id, Constants.CANCELADA);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/en-progreso") // <localhost>/incidencias/cancela POST
	private ResponseEntity<String> activaIncidencia(@RequestParam(required = true) long id)
	{

		return modificaEstadoIncidencia(id, Constants.EN_PROGRESO);

	}

	@PostMapping(value = "/filtro") // <localhost>/incidencias/filtro2 POST
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
				return Constants.ID_NO_ENCONTRADO;
			}
		} catch (Exception e)
		{
			log.error("Excepción capturada en modificaEstadoIncidencia: {}", e.getMessage(), e);
			return Constants.ERROR_INESPERADO;
		}
	}

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
					incidencia.setComentarioSolucion(comentarioSolucion);

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

	private boolean controlaNuloBlanco(String cadena)
	{
		return cadena == null || cadena.isBlank();
	}

}
