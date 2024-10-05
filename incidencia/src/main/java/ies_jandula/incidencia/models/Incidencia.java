package ies_jandula.incidencia.models;

import java.util.Date;
import java.util.Objects;

import org.aspectj.weaver.ast.Instanceof;

//import ies_jandula.incidencia.utils.EstadoIncidencia;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Incidencia implements Comparable
{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // Automaticamente genera el campo ID.
	@Column(name = "id_incidencia")
	long id;

	String numeroAula;

	String correoDocente;

	Date fechaIncidencia;

	String descripcionIncidencia;

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

	public String getCorreoDocente()
	{
		return correoDocente;
	}

	public void setCorreoDocente(String correoDocente)
	{
		this.correoDocente = correoDocente;
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

	public String getEstadoIncidencia()
	{
		return estadoIncidencia;
	}

	public void setEstadoIncidencia(String estado)
	{
		this.estadoIncidencia = estado;
	}

	// Metodos de comparación.

	@Override
	public int hashCode()
	{
		return Objects.hash(descripcionIncidencia, estadoIncidencia, fechaIncidencia, id, correoDocente, numeroAula);
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
				&& id == other.id && Objects.equals(correoDocente, other.correoDocente)
				&& Objects.equals(numeroAula, other.numeroAula);
	}

	// toString generico.

	@Override
	public String toString()
	{
		return "Incidencia [id=" + id + ", numeroAula=" + numeroAula + ", correoDocente=" + correoDocente
				+ ", fechaIncidencia=" + fechaIncidencia + ", descripcionIncidencia=" + descripcionIncidencia
				+ ", estadoIncidencia=" + estadoIncidencia + "]";
	}

	@Override
	public int compareTo(Object o)
	{

		if (!(o instanceof Incidencia))
		{
			return 0;
		}

		Incidencia other = (Incidencia) o;

		if (this.fechaIncidencia == null && other.fechaIncidencia == null)
		{
			return 0; // Both are null
		}
		if (this.fechaIncidencia == null)
		{
			return -1; // This is considered less than o
		}
		if (other.fechaIncidencia == null)
		{
			return 1; // o is considered less than this
		}

		return this.fechaIncidencia.compareTo(other.fechaIncidencia);

	}

}
