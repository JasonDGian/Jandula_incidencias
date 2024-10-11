package ies.jandula.incidencia.models;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "incidencia")
public class Incidencia 
{

	/**
	 * Atribtuo - Identificador unico auto-generado de la incidencia en BBDD.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Automaticamente genera el campo ID.
	@Column(name = "id_incidencia")
	private long id;

	/**
	 * Atribtuo - Aula en la que se da la incidencia.
	 */
	private String numeroAula;

	/**
	 * Atribtuo - Correo del docente que informa de la incidencia.
	 */
	private String correoDocente;

	/**
	 * Atribtuo - Fecha de creación de la señalación.
	 */
	private Date fechaIncidencia;

	/**
	 * Atribtuo - Detalla el problema relacionado a la incidencia.
	 */
	private String descripcionIncidencia;

	/**
	 * Atribtuo - Define el estado de la incidencia. 
	 */
	private String estadoIncidencia;
	
	/**
	 * Atribtuo - Comentario relacionado a la solucion de la incidencia.
	 */
	private String comentario;

}
