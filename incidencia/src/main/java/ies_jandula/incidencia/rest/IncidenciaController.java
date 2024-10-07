package ies_jandula.incidencia.rest;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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
// import ies_jandula.incidencia.utils.EstadoIncidencia; DESACTIVADO A FAVOR DE TIPO STRING

@RestController
@RequestMapping(value = "/incidencias")
public class IncidenciaController
{

	@Autowired
	IIncidenciaRepository repo;

	// Adaptar toda la logica del controlador al uso de este repositorio.
	// Gestionar las excepciones y validaciones desde el controlador.

	// LISTAR TODASS INCIDENCIAS.
	@GetMapping // <localhost>/incidencias GET
	public List<Incidencia> listarIncidencias()
	{
		return repo.findAll();
	}

	@PostMapping // <localhost>:8888/incidencias POST
	public Optional<Incidencia> buscaIncidencia(@RequestParam long id)
	{
		// Metodo que devuelve un optional porque no sabemos si el ID buscado
		// existe ne la bbdd o no.

		return repo.findById(id);
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
	public String reiniciaIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.PENDIENTE);
	}

	// Cambiar a RESUELTA la incidencia con el ID proporcionado.
	@PostMapping(value = "/resuelve") // <localhost>/incidencias/resuelve POST
	public String resuelveIncidencia(@RequestParam(required = true) long id)
	{
		return modificaEstadoIncidencia(id, Constants.RESUELTA);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/cancelar") // <localhost>/incidencias/cancela POST
	public String cancelaIncidencia(@RequestParam(required = true) long id)
	{

		return modificaEstadoIncidencia(id, Constants.CANCELADA);
	}

	// Cambiar a CANCELADA la incidencia con el ID proporcionado.
	@PostMapping(value = "/en-progreso") // <localhost>/incidencias/cancela POST
	public String activaIncidencia(@RequestParam(required = true) long id)
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
	 * Metodo que recibe un id de una incidencia y un estado para aplicarle. Si no
	 * existen incidencias con ese ID devuelve un mensaje (String) que informa. Si
	 * la incidencia ya está en ese estado devuelve un aviso (String) y si la
	 * operacion se lleva a cabo con éxito devuelve un mensaje de informe.
	 * 
	 * @param id
	 * @param estado
	 * @return
	 */
	private String modificaEstadoIncidencia(long id, String estado)
	{

		Optional<Incidencia> optIncidencia = repo.findById(id);

		if (optIncidencia.isPresent())
		{
			Incidencia incidencia = optIncidencia.get();

			if (incidencia.getEstadoIncidencia().equals(estado))
			{
				return "AVISO: La incidencia ya estaba configurada como " + estado + ".";
			} else
			{
				incidencia.setEstadoIncidencia(estado);
				repo.saveAndFlush(incidencia);
				// llamar a metodo de aviso a profesor (mandar correo de aviso).
				return "Incidencia modificada a " + estado + " con éxito.";
			}
		} else
		{
			return "ERROR: No existen incidencias con ese ID.";
		}
	}
}
