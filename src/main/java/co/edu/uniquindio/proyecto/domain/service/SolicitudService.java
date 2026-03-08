package co.edu.uniquindio.proyecto.domain.service;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

public class SolicitudService {

    public Solicitud registrarSolicitud(Usuario estudiante,
                                        CodigoSolicitud codigo,
                                        TipoCanal canal,
                                        DescripcionSolicitud descripcion) {

        if (!estudiante.puedeRegistrarSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un estudiante puede registrar solicitudes");
        }

        return new Solicitud(
                codigo,
                estudiante.getId(),
                canal,
                descripcion
        );
    }

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

    public void iniciarAtencionSolicitud(Solicitud solicitud,
                                         Usuario funcionario) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede atender solicitudes");
        }

        solicitud.iniciarAtencion(
                funcionario.getId()
        );
    }

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