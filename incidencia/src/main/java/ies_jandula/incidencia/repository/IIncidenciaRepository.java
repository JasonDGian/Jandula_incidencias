package ies_jandula.incidencia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import ies_jandula.incidencia.models.Incidencia;

@Repository
public interface IIncidenciaRepository extends JpaRepository<Incidencia, Long>
{
    // Devuelve un listado de incidencias basándose en el estado de la incidencia.
    public List<Incidencia> findByEstadoIncidencia(String estado);
    
    // Método de filtrado por estado con query personalizada.
    @Query("SELECT i FROM Incidencia i WHERE i.estadoIncidencia = :estado ORDER BY i.fechaIncidencia DESC")
    public List<Incidencia> findByEstadoIncidenciaOrderDesc(@Param("estado") String estado);
    
    // Método de filtrado por estado con query personalizada.
    @Query("SELECT i FROM Incidencia i WHERE i.estadoIncidencia = :estado ORDER BY i.fechaIncidencia ASC")
    public List<Incidencia> findByEstadoIncidenciaOrderAsc(@Param("estado") String estado);
}
