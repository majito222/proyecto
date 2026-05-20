package co.edu.uniquindio.proyecto.application;

import co.edu.uniquindio.proyecto.domain.valueobject.DescripcionSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.PrioridadSolicitud;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoCanal;
import co.edu.uniquindio.proyecto.domain.valueobject.TipoSolicitud;

public class PriorizarSolicitudIAService {

    public PrioridadSolicitud sugerir(TipoCanal canal, TipoSolicitud tipo, DescripcionSolicitud descripcion) {
        String texto = descripcion.valor().toLowerCase();

        if (texto.contains("urgente") || texto.contains("matricula") || tipo == TipoSolicitud.SOLICITUD_CUPO) {
            return new PrioridadSolicitud(PrioridadSolicitud.Nivel.ALTA, "Prioridad sugerida automaticamente por el tipo o descripcion.");
        }

        if (canal == TipoCanal.CSU || tipo == TipoSolicitud.HOMOLOGACION) {
            return new PrioridadSolicitud(PrioridadSolicitud.Nivel.MEDIA, "Prioridad media sugerida por canal o tipo de solicitud.");
        }

        return new PrioridadSolicitud(PrioridadSolicitud.Nivel.BAJA, "Prioridad baja sugerida automaticamente.");
    }
}
