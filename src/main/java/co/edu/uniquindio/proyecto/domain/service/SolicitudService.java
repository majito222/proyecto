package co.edu.uniquindio.proyecto.domain.service;
import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.entity.Usuario;
import co.edu.uniquindio.proyecto.domain.exception.RolNoAutorizadoException;
import co.edu.uniquindio.proyecto.domain.valueobject.*;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

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
                estudiante.getNombre(),
                canal,
                tipo,
                descripcion
        );
    }

    /**
     * Clasifica una solicitud como funcionario.
     *
     * @param solicitud solicitud a clasificar
     * @param funcionario usuario funcionario
     * @param tipo tipo de solicitud
     */
    public void clasificarSolicitud(Solicitud solicitud,
                                    Usuario funcionario,
                                    TipoSolicitud tipo) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede clasificar solicitudes");
        }

        solicitud.clasificarSolicitud(
                tipo,
                funcionario.getId()
        );
    }

    /**
     * Asigna prioridad a una solicitud como funcionario.
     *
     * @param solicitud solicitud a priorizar
     * @param funcionario usuario funcionario
     * @param prioridad prioridad asignada
     */
    public void priorizarSolicitud(Solicitud solicitud,
                                   Usuario funcionario,
                                   PrioridadSolicitud prioridad) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede priorizar solicitudes");
        }

        solicitud.asignarPrioridad(
                prioridad,
                funcionario.getId()
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
     * Marca una solicitud como atendida por funcionario.
     *
     * @param solicitud solicitud atendida
     * @param funcionario usuario funcionario
     */
    public void marcarSolicitudAtendida(Solicitud solicitud,
                                        Usuario funcionario) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede marcar solicitudes como atendidas");
        }

        solicitud.marcarAtendida(
                funcionario.getId()
        );
    }

    /**
     * Cierra una solicitud como funcionario.
     *
     * @param solicitud solicitud a cerrar
     * @param funcionario usuario funcionario
     * @param observacion observacion del cierre
     */
    public void cerrarSolicitud(Solicitud solicitud,
                                Usuario funcionario,
                                String observacion) {

        if (!funcionario.puedeAtenderSolicitudes()) {
            throw new RolNoAutorizadoException("Solo un funcionario puede cerrar solicitudes");
        }

        solicitud.cerrarSolicitud(
                funcionario.getId(),
                observacion
        );
    }

    /**
     * Busca el historial de solicitudes por id de estudiante.
     *
     * @param solicitudes lista de solicitudes
     * @param estudianteId id del estudiante
     * @return historial combinado del estudiante
     */
    public List<Historial> buscarHistorialPorEstudianteId(List<Solicitud> solicitudes,
                                                          IdUsuario estudianteId) {

        Objects.requireNonNull(solicitudes, "La lista de solicitudes no puede ser nula");
        Objects.requireNonNull(estudianteId, "El id del estudiante no puede ser nulo");

        return solicitudes.stream()
                .filter(solicitud -> estudianteId.equals(solicitud.getEstudianteId()))
                .flatMap(solicitud -> solicitud.obtenerHistorial().stream())
                .sorted(Comparator.comparing(Historial::fecha))
                .toList();
    }

    /**
     * Busca el historial de solicitudes por nombre de estudiante.
     *
     * @param solicitudes lista de solicitudes
     * @param nombre nombre del estudiante
     * @return historial combinado del estudiante
     */
    public List<Historial> buscarHistorialPorEstudianteNombre(List<Solicitud> solicitudes,
                                                              String nombre) {

        Objects.requireNonNull(solicitudes, "La lista de solicitudes no puede ser nula");
        Objects.requireNonNull(nombre, "El nombre del estudiante no puede ser nulo");

        String nombreNormalizado = nombre.trim();
        if (nombreNormalizado.isEmpty()) {
            throw new IllegalArgumentException("El nombre del estudiante no puede ser vacio");
        }

        return solicitudes.stream()
                .filter(solicitud -> solicitud.getEstudianteNombre().equalsIgnoreCase(nombreNormalizado))
                .flatMap(solicitud -> solicitud.obtenerHistorial().stream())
                .sorted(Comparator.comparing(Historial::fecha))
                .toList();
    }
}
