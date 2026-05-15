package co.edu.uniquindio.proyecto;

import co.edu.uniquindio.proyecto.infrastructure.security.config.DefaultUserProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

/**
 * Punto de entrada de la aplicacion Spring Boot.
 */
@SpringBootApplication
@EnableConfigurationProperties({
		DefaultUserProperties.class
})
public class ProyectoApplication {

	/**
	 * Arranca el contexto de Spring.
	 *
	 * @param args argumentos de linea de comandos
	 */
	public static void main(String[] args) {
		SpringApplication.run(ProyectoApplication.class, args);
	}
}