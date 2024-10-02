package ies_jandula.incidencia.persistence.model;

import java.util.Date;
import java.util.Objects;

//import ies_jandula.incidencia.utils.EstadoIncidencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Incidencia
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)  // Automaticamente genera el campo ID.
	@Column(name = "id_incidencia")
	long id;

	String numeroAula;

	String nombreProfesor;

	Date fechaIncidencia;

	String descripcionIncidencia;

	//EstadoIncidencia estadoIncidencia;
	String estadoIncidencia;

	// Constructor.
	public Incidencia()
	{
		// Constructor vacio por defecto.
	}

	// Getters y setters.
	public long getId()
	{
		return id;
	}

	public void setId(long id)
	{
		this.id = id;
	}

	public String getNumeroAula()
	{
		return numeroAula;
	}

	public void setNumeroAula(String numeroAula)
	{
		this.numeroAula = numeroAula;
	}

	public String getNombreProfesor()
	{
		return nombreProfesor;
	}

	public void setNombreProfesor(String nombreProfesor)
	{
		this.nombreProfesor = nombreProfesor;
	}

	public Date getFechaIncidencia()
	{
		return fechaIncidencia;
	}

	public void setFechaIncidencia(Date fechaIncidencia)
	{
		this.fechaIncidencia = fechaIncidencia;
	}

	public String getDescripcionIncidencia()
	{
		return descripcionIncidencia;
	}

	public void setDescripcionIncidencia(String descripcionIncidencia)
	{
		this.descripcionIncidencia = descripcionIncidencia;
	}

//	public EstadoIncidencia getEstadoIncidencia()
//	{
//		return estadoIncidencia;
//	}
//
//	public void setEstadoIncidencia(EstadoIncidencia estadoIncidencia)
//	{
//		this.estadoIncidencia = estadoIncidencia;
//	}
	
	public String getEstadoIncidencia() {
		return estadoIncidencia;
	}
	
	public void setEstadoIncidencia( String estado ) {
		this.estadoIncidencia = estado;
	}

	// Metodos de comparación.

	@Override
	public int hashCode()
	{
		return Objects.hash(descripcionIncidencia, estadoIncidencia, fechaIncidencia, id, nombreProfesor, numeroAula);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Incidencia other = (Incidencia) obj;
		return Objects.equals(descripcionIncidencia, other.descripcionIncidencia)
				&& estadoIncidencia == other.estadoIncidencia && Objects.equals(fechaIncidencia, other.fechaIncidencia)
				&& id == other.id && Objects.equals(nombreProfesor, other.nombreProfesor)
				&& Objects.equals(numeroAula, other.numeroAula);
	}

	// toString generico.

	@Override
	public String toString()
	{
		return "Incidencia [id=" + id + ", numeroAula=" + numeroAula + ", nombreProfesor=" + nombreProfesor
				+ ", fechaIncidencia=" + fechaIncidencia + ", descripcionIncidencia=" + descripcionIncidencia
				+ ", estadoIncidencia=" + estadoIncidencia + "]";
	}

}
