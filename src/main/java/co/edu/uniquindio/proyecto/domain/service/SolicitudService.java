package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

/**
 * Servicio de dominio para orquestar operaciones sobre solicitudes.
 */
public class SolicitudService {

    /**
     * Registra una solicitud para un estudiante.
     *
     * @param estudiante usuario que registra la solicitud
     * @param codigo codigo de la solicitud
     * @param canal canal de registro
     * @param tipo tipo de solicitud
     * @param descripcion descripcion inicial
     * @return nueva solicitud en estado REGISTRADA
     */
    public Solicitud registrarSolicitud(Usuario estudiante,
                                        CodigoSolicitud codigo,
                                        TipoCanal canal,
                                        TipoSolicitud tipo,
                                        DescripcionSolicitud descripcion) {

        if (!estudiante.puedeRegistrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un estudiante puede registrar solicitudes");
        }

        return new Solicitud(
                codigo,
                estudiante.getId(),
                canal,
                tipo,
                descripcion
        );
    }

    /**
     * Clasifica una solicitud como administrador.
     *
     * @param solicitud solicitud a clasificar
     * @param administrador usuario administrador
     * @param tipo tipo de solicitud
     */
    public void clasificarSolicitud(Solicitud solicitud,
                                    Usuario administrador,
                                    TipoSolicitud tipo) {

        if (!administrador.puedeAdministrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un administrador puede clasificar solicitudes");
        }

        solicitud.clasificarSolicitud(
                tipo,
                administrador.getId()
        );
    }

    /**
     * Asigna prioridad a una solicitud como administrador.
     *
     * @param solicitud solicitud a priorizar
     * @param administrador usuario administrador
     * @param prioridad prioridad asignada
     */
    public void priorizarSolicitud(Solicitud solicitud,
                                   Usuario administrador,
                                   PrioridadSolicitud prioridad) {

        if (!administrador.puedeAdministrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un administrador puede priorizar solicitudes");
        }

        solicitud.asignarPrioridad(
                prioridad,
                administrador.getId()
        );
    }

    /**
     * Inicia la atencion de una solicitud como funcionario.
     *
     * @param solicitud solicitud a atender
     * @param funcionario usuario funcionario
     */
    public void iniciarAtencionSolicitud(Solicitud solicitud,
                                         Usuario funcionario) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede atender solicitudes");
        }

        solicitud.iniciarAtencion(
                funcionario.getId()
        );
    }

    /**
     * Cierra una solicitud como administrador.
     *
     * @param solicitud solicitud a cerrar
     * @param administrador usuario administrador
     * @param observacion observacion del cierre
     */
    public void cerrarSolicitud(Solicitud solicitud,
                                Usuario administrador,
                                String observacion) {

        if (!administrador.puedeAdministrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un administrador puede cerrar solicitudes");
        }

        solicitud.cerrarSolicitud(
                administrador.getId(),
                observacion
        );
    }
}
