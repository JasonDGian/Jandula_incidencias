package ies.jandula.incidencia.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import ies.jandula.incidencia.dto.IncidenciaDTO;
import ies.jandula.incidencia.entity.IncidenciaEntity;
import ies.jandula.incidencia.entity.IncidenciaEntityId;

@Repository
public interface IIncidenciaRepository extends JpaRepository<IncidenciaEntity, IncidenciaEntityId>
{

	@Query("SELECT new ies.jandula.incidencia.dto.IncidenciaDTO("
			+ "e.numeroAula, e.correoDocente, e.fechaIncidencia, e.descripcionIncidencia, e.estadoIncidencia, e.comentario"
			+ ") " + "FROM IncidenciaEntity e WHERE ( :numeroAula IS NULL OR e.numeroAula = :numeroAula ) AND "
			+ "( :correoDocente IS NULL OR e.correoDocente = :correoDocente ) AND "
			+ "( :fechaInicio IS NULL OR :fechaInicio <= e.fechaIncidencia ) AND "
			+ "( :fechaFin IS NULL OR :fechaFin >= e.fechaIncidencia ) AND "
			+ "( :descripcionIncidencia IS NULL OR e.descripcionIncidencia LIKE CONCAT('%', :descripcionIncidencia, '%') ) AND "
			+ "( :estadoIncidencia IS NULL OR e.estadoIncidencia = :estadoIncidencia ) AND "
			+ "( :comentario IS NULL OR e.comentario LIKE CONCAT('%', :comentario, '%') )")
	public List<IncidenciaDTO> buscaIncidencia(  
			String numeroAula, 
			String correoDocente, 
			LocalDateTime fechaInicio, 
			LocalDateTime fechaFin, 
			String descripcionIncidencia, 
			String estadoIncidencia, 
			String comentario );

}
