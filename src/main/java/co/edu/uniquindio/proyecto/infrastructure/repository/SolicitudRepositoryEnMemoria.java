package co.edu.uniquindio.proyecto.infrastructure.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.repository.SolicitudRepository;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Implementación en memoria del repositorio de solicitudes.
 */
@Repository
public class SolicitudRepositoryEnMemoria implements SolicitudRepository {

    private final Map<String, Solicitud> solicitudes = new ConcurrentHashMap<>();

    @Override
    public Solicitud guardar(Solicitud solicitud) {
        String clave = solicitud.getCodigo().valor();
        solicitudes.put(clave, solicitud);
        return solicitud;
    }

    @Override
    public Solicitud buscarPorCodigo(CodigoSolicitud codigo) {
        Solicitud solicitud = solicitudes.get(codigo.valor());
        if (solicitud == null) {
            throw new NoSuchElementException("Solicitud no encontrada: " + codigo.valor());
        }
        return solicitud;
    }

    @Override
    public List<Solicitud> buscarPorEstado(EstadoSolicitud estado) {
        return solicitudes.values().stream()
                .filter(solicitud -> solicitud.getEstado() == estado)
                .toList();
    }

    @Override
    public List<Solicitud> buscarPorEstudiante(IdUsuario estudianteId) {
        return solicitudes.values().stream()
                .filter(solicitud -> solicitud.getEstudianteId().equals(estudianteId))
                .toList();
    }

    @Override
    public List<Solicitud> listarTodas() {
        return new ArrayList<>(solicitudes.values());
    }

    @Override
    public void eliminarPorCodigo(CodigoSolicitud codigo) {
        solicitudes.remove(codigo.valor());
    }
}