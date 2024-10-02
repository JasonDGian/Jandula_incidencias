package ies_jandula.incidencia.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ies_jandula.incidencia.persistence.model.Incidencia;

@Repository
public interface IncidenciaRepository extends JpaRepository<Incidencia, Long>
{
	// Devuelve un listado de incidencias basandose en el estado de la incidencia.	
	public List<Incidencia> findByEstadoIncidencia( String estado );
}
