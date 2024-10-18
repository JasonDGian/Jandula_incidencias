package ies.jandula.incidencia.utils;

public final class Constants
{
	// Constantes de estado de incidencias
	public static final String EN_PROGRESO = "EN PROGRESO";
	public static final String CANCELADA = "CANCELADA";
	public static final String RESUELTA = "RESUELTA";
	public static final String PENDIENTE = "PENDIENTE";

	// Constantes para maximos de longitud de campo.
		// Comentario de resolucion de incidencia.
		public static final int MAX_LONG_COMENTARIO = 150;
		// Estado de la incidencia.
		public static final int MAX_LONG_ESTADO = 12;
		// Correo del docente que señala.
		public static final int MAX_LONG_CORREO = 50;
		// Descripcion de la incidencia.
		public static final int MAX_LONG_DESCRIPCION = 250;

		
		
		private Constants() {
			// Constructor privado que oculta el publico.
		}
}
