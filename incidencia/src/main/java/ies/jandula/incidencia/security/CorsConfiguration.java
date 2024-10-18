package ies.jandula.incidencia.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuración CORS para la aplicación.
 * <p>
 * Esta clase define una configuración personalizada para los mapeos de CORS (Cross-Origin Resource Sharing).
 * Utiliza un bean para habilitar y configurar el acceso desde diferentes orígenes al endpoint "/incidencias".
 * <p>
 * El propósito de esta configuración es permitir que clientes desde diferentes dominios puedan realizar
 * solicitudes HTTP al servicio de incidencias.
 * </p>
 */
@Configuration
public class CorsConfiguration
{
	
    /**
     * Configura las reglas CORS para el endpoint "/incidencias".
     * <p>
     * Este método devuelve una implementación de {@link WebMvcConfigurer} que sobrescribe el método
     * {@code addCorsMappings} para permitir el intercambio de recursos entre orígenes en el endpoint "/incidencias".
     * </p>
     *
     * @return una instancia de {@link WebMvcConfigurer} con la configuración CORS personalizada.
     */
	@Bean
	public WebMvcConfigurer corsConfigurer()
	{

		return new WebMvcConfigurer()
		{
			@Override
			public void addCorsMappings(CorsRegistry registry)
			{
				// Configura el mapeo CORS para el endpoint /incidencias
				// Esto para poder montar luego el frontend.
				registry.addMapping("/incidencias");
			}
		};

	}
}
