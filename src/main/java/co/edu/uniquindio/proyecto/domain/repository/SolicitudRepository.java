package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz de repositorio para el agregado Solicitud.
 */
public interface SolicitudRepository {

    /**
     * Guarda una solicitud (crea o actualiza).
     * @param solicitud solicitud a guardar
     * @return solicitud guardada
     */
    Solicitud guardar(Solicitud solicitud);

    /**
     * Busca una solicitud por su código.
     * @param codigo código de la solicitud
     * @return solicitud encontrada
     */
    Solicitud buscarPorCodigo(CodigoSolicitud codigo);

    /**
     * Busca solicitudes por estado.
     * param estado estado a filtrar
     * @return lista de solicitudes en ese estado
     */
    List<Solicitud> buscarPorEstado(EstadoSolicitud estado);

    /**
     * Busca solicitudes por estudiante.
     * @param estudianteId identificador del estudiante
     * @return lista de solicitudes del estudiante
     */
    List<Solicitud> buscarPorEstudiante(IdUsuario estudianteId);

    /**
     * Lista todas las solicitudes.
     * @return lista de todas las solicitudes
     */
    List<Solicitud> listarTodas();

    /**
     * Elimina una solicitud por su código.
     * @param codigo código de la solicitud a eliminar
     */
    void eliminarPorCodigo(CodigoSolicitud codigo);
}
