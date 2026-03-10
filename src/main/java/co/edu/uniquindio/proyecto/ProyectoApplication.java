package co.edu.uniquindio.proyecto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Punto de entrada de la aplicacion Spring Boot.
 */
@SpringBootApplication
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
