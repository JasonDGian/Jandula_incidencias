package ies_jandula.incidencia.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies_jandula.incidencia.persistence.model.Incidencia;
import ies_jandula.incidencia.utils.EstadoIncidencia;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>
{
	// Devuelve un listado de incidencias basandose en el estado de la incidencia.
	// public List<Incidencia> findByEstadoIncidencia( EstadoIncidencia estadoIncidencia);
	
}
