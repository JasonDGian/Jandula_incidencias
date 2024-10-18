package ies.jandula.incidencia;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal de la aplicación que arranca el servicio de gestión de incidencias.
 * <p>
 * Esta clase se encarga de iniciar la aplicación Spring Boot. Al ejecutarla, se inicializa el contexto de la aplicación
 * y se cargan todos los componentes necesarios para el funcionamiento del servicio.
 * </p>
 */
@SpringBootApplication
public class IncidenciaApplication {

	/**
	 * Metodo main que lanza la aplicación Spring en si misma.
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(IncidenciaApplication.class, args);
	}

}
