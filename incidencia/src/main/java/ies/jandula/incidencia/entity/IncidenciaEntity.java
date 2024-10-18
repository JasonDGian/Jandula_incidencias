package ies.jandula.incidencia.entity;

import java.time.LocalDateTime;

import ies.jandula.incidencia.utils.Constants;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * Clase incidencia.
 * Define el objeto de incidencia que es tratado y almacenado en la base de datos.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "incidencias")
@IdClass(IncidenciaEntityId.class)
public class IncidenciaEntity 
{

	
	/**
	 * Atributo - Aula en la que se da la incidencia.
	 */
	@Id
	private String numeroAula;

	/**
	 * Atributo - Correo del docente que informa de la incidencia.
	 */
	@Id
	@Column(length = Constants.MAX_LONG_CORREO)
	private String correoDocente;

	/**
	 * Atributo - Fecha de creación de la señalación.
	 */
	@Id
	private LocalDateTime fechaIncidencia;
	
	/**
	 * Atributo - Detalla el problema relacionado a la incidencia.
	 */
	@Column(length = Constants.MAX_LONG_DESCRIPCION)
	private String descripcionIncidencia;

	/**
	 * Atributo - Define el estado de la incidencia. 
	 */
	@Column(length = Constants.MAX_LONG_ESTADO)
	private String estadoIncidencia;
	
	/**
	 * Atributo - Comentario relacionado a la solucion de la incidencia.
	 */
	@Column(length = Constants.MAX_LONG_COMENTARIO)
	private String comentario;

}
