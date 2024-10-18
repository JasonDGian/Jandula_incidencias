package ies.jandula.incidencia.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies.jandula.incidencia.dto.IncidenciaDTO;
import ies.jandula.incidencia.entity.IncidenciaEntity;
import ies.jandula.incidencia.entity.IncidenciaEntityId;

/**
 * Repositorio para gestionar incidencias en la base de datos.
 * <p>
 * Esta interfaz extiende {@link JpaRepository} y proporciona métodos para realizar operaciones
 * de búsqueda y verificación sobre la entidad {@link IncidenciaEntity} utilizando identificadores compuestos.
 * </p>
 */
@Repository
public interface IIncidenciaRepository extends JpaRepository<IncidenciaEntity, IncidenciaEntityId>
{
	
	/**
	 * Verifica si existe una incidencia en la base de datos utilizando un identificador compuesto.
	 * <p>
	 * Este método crea un identificador compuesto a partir del número de aula, el correo del docente y la fecha de la incidencia,
	 * y luego utiliza este identificador para comprobar si la incidencia correspondiente ya está registrada en la base de datos.
	 * </p>
	 *
	 * @param numeroAula          El número del aula asociado a la incidencia.
	 * @param correoDocente       El correo del docente que reportó la incidencia.
	 * @param fechaIncidencia     La fecha y hora en que ocurrió la incidencia.
	 * @return                   {@code true} si la incidencia existe en la base de datos; {@code false} en caso contrario.
	 */
	public default boolean existsByCompositeId( String numeroAula, String correoDocente, LocalDateTime fechaIncidencia  ) {
		IncidenciaEntityId id = new IncidenciaEntityId( numeroAula, correoDocente, fechaIncidencia  );
		return this.existsById(id);
	}
		
	
	/**
	 * Busca incidencias en la base de datos según los criterios especificados.
	 * <p>
	 * Este método utiliza una consulta JPQL para seleccionar incidencias y devolver una lista de objetos {@link IncidenciaDTO}.
	 * Los criterios de búsqueda son opcionales y se aplican si se proporcionan valores diferentes de null.
	 * </p>
	 * 
	 * Cada parametro especificado a continuación puede ser nulo. De serlo será ignorado en la busqueda.	  
	 * @param numeroAula              El número del aula de la incidencia.
	 * @param correoDocente           El correo del docente que reportó la incidencia.
	 * @param fechaInicio             La fecha y hora de inicio para filtrar incidencias.
	 * @param fechaFin                La fecha y hora de fin para filtrar incidencias.
	 * @param descripcionIncidencia    Parte de la descripción de la incidencia a buscar.
	 * @param estadoIncidencia        El estado de la incidencia.
	 * @param comentario               Parte del comentario de la incidencia a buscar.
	 * @return                        Una lista de objetos {@link IncidenciaDTO} que cumplen con los criterios de búsqueda.
	 */
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
			@Param("numeroAula") String numeroAula, 
			@Param("correoDocente")String correoDocente, 
			@Param("fechaInicio")LocalDateTime fechaInicio, 
			@Param("fechaFin")LocalDateTime fechaFin, 
			@Param("descripcionIncidencia")String descripcionIncidencia, 
			@Param("estadoIncidencia")String estadoIncidencia, 
			@Param("comentario")String comentario );

}
