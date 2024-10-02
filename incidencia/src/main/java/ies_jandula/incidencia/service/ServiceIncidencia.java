package ies_jandula.incidencia.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ies_jandula.incidencia.persistence.model.Incidencia;
import ies_jandula.incidencia.persistence.repository.IncidenciaRepository;
//import ies_jandula.incidencia.utils.EstadoIncidencia;
import jakarta.transaction.Transactional;

@Service
public class ServiceIncidencia
{

	@Autowired
	private IncidenciaRepository inRepo;

	@Transactional
	public String crearIncidencia(Incidencia incidencia)
	{
		inRepo.saveAndFlush(incidencia);
		return "Hecho";
	}

	// Muestra un listado con todas las incidencias en la BBDD
	public List<Incidencia> listaIncidencias()
	{
		return inRepo.findAll();
	}

	// Modificar incidencia
	public String modificarEstadoResuelta(Long id)
	{
		if (actualizaIncidencia(id, "RESUELTA"))
		{
			return "Incidencia resuelta.";

		} else
		{
			return "Incidencia con id " + id + " no existe";
		}

	}

	// Cancelar incidencia
	// Modificar incidencia
	public String modificarEstadoCancela(Long id)
	{
		if (actualizaIncidencia(id, "CANCELADA"))
		{
			return "Incidencia cancelada.";

		} else
		{
			return "Incidencia con id " + id + " no existe";
		}
	}

	// FUNCION ACTUALIZAR ESTADO.
	// Si el proceso se realiza con exito devuelve TRUE y de lo contrario FALSE.
	private boolean actualizaIncidencia(Long id, String estado)
	{
		// Busca el objeto.
		Optional<Incidencia> incidencia = inRepo.findById(id);

		// Si el objeto existe en BBDD
		if (incidencia.isPresent())
		{
			// Crea un nuevo objeto para guardarlo.
			Incidencia inciNueva = incidencia.get();
			inciNueva.setEstadoIncidencia(estado);
			inRepo.saveAndFlush(inciNueva);
			return true;
		} else
		{
			return false;
		}
	}

	// Ver todas incidencias.
	// ver incidencia filtro.

}
