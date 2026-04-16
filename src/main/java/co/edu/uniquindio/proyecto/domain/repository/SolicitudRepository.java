package co.edu.uniquindio.proyecto.domain.repository;

import co.edu.uniquindio.proyecto.domain.entity.Solicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.CodigoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.EstadoSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.IdUsuario;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SolicitudRepository {

    Solicitud guardar(Solicitud solicitud);

    Solicitud buscarPorCodigo(CodigoSolicitud codigo);

    List<Solicitud> buscarPorEstado(EstadoSolicitud estado);

    List<Solicitud> buscarPorEstudiante(IdUsuario estudianteId);

    List<Solicitud> listarTodas();

    void eliminarPorCodigo(CodigoSolicitud codigo);

    boolean existePorCodigo(CodigoSolicitud codigo);

    List<Solicitud> buscarPorEstadoPrioridad(EstadoSolicitud estado);

    List<Solicitud> buscarSinAsignarAltaPrioridad(PrioridadSolicitud.Nivel nivel);

    List<Solicitud> buscarPorCodigoParcial(String codigoParcial);

    List<Solicitud> buscarPorTexto(String texto);

    Page<Solicitud> buscarActivasPaginadas(Pageable pageable);

    Page<Solicitud> buscarPorFiltros(EstadoSolicitud estado, TipoCanal canal, Pageable pageable);

    Optional<Solicitud> buscarConHistorial(CodigoSolicitud codigo);

    List<Solicitud> buscarPorVariosEstados(List<EstadoSolicitud> estados);

    long contarPorEstado(EstadoSolicitud estado);
}
