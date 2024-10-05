package ies_jandula.incidencia.rest;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ies_jandula.incidencia.models.Incidencia;
import ies_jandula.incidencia.repository.IIncidenciaRepository;
import ies_jandula.incidencia.utils.Constants;
// import ies_jandula.incidencia.utils.EstadoIncidencia; DESACTIVADO A FAVOR DE TIPO STRING

@RestController
public class IncidenciaController
{

	@Autowired
	IIncidenciaRepository repo;

	// Adaptar toda la logica del controlador al uso de este repositorio.
	// Gestionar las excepciones y validaciones desde el controlador.

	// LISTAR TODASS INCIDENCIAS.
	@RequestMapping(value = "/incidencias", method = RequestMethod.GET)
	public List<Incidencia> listarIncidencias()
	{
		return repo.findAll();
	}

	// Generar una nueva incidencia.
	@PostMapping(value = "/nueva") // <localhost>/incidencias/nueva
	public String creaIncidencia(@RequestHeader(value = "correoDocente", required = true) String correoDocente,
			@RequestBody Incidencia incidencia)
	{
		/*
		 * HACER BLOQUE DE VALIDACIÓN DE OBJETO JSON INCIDENCIA.
		 */

		// Asignar el correo al objeto.
		incidencia.setCorreoDocente(correoDocente);
		// Inicializar el estado de la incidencia by default.
		incidencia.setEstadoIncidencia(Constants.EN_PROGRESO);

		// Automatiza la asignación de la fecha.
		// Hora española.
		ZonedDateTime currentTimeInSpain = ZonedDateTime.now(java.time.ZoneId.of("Europe/Madrid"));
		incidencia.setFechaIncidencia(Date.from(currentTimeInSpain.toInstant()));

		repo.saveAndFlush(incidencia);

		return "Incidencia creada " + incidencia.toString();
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelta") // <localhost>/incidencias/resuelta
	public String resuelveIncidencia(@RequestParam(required = true) long id)
	{

		Optional<Incidencia> optIncidencia = repo.findById(id);

		if (optIncidencia.isPresent())
		{
			Incidencia incidencia = optIncidencia.get();

			if (incidencia.getEstadoIncidencia().equals(Constants.RESUELTA))
			{
				return "AVISO: Incidencia ya resuelta.";
			} else
			{
				incidencia.setEstadoIncidencia(Constants.RESUELTA);
				repo.saveAndFlush(incidencia);
				return "Incidencia resuelta con éxito.";
			}
		} else
		{
			return "ERROR: No existen incidencias con ese ID.";
		}
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancelar
	public String cancelaIncidencia(@RequestParam(required = true) long id)
	{

		Optional<Incidencia> optIncidencia = repo.findById(id);

		if (optIncidencia.isPresent())
		{
			Incidencia incidencia = optIncidencia.get();

			if (incidencia.getEstadoIncidencia().equals(Constants.CANCELADA))
			{
				return "AVISO: Incidencia ya cancelada.";
			} else
			{
				incidencia.setEstadoIncidencia(Constants.CANCELADA);
				repo.saveAndFlush(incidencia);
				return "Incidencia cancelada con éxito.";
			}
		} else
		{
			return "ERROR: No existen incidencias con ese ID.";
		}
	}

	// Buscar incidencia por filtro.
	@PostMapping(value = "/filtro") // <localhost>/incidencias/filter
	public List<Incidencia> filtrarIncidencia(@RequestBody String estado)
	{
		return repo.findByEstadoIncidencia(estado);
	}

}
