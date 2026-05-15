package co.edu.uniquindio.proyecto.application.dto.response;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

public record PaginaResponse<T>(
        List<T> contenido,
        int pagina,
        int tamano,
        long totalElementos,
        int totalPaginas,
        boolean primera,
        boolean ultima,
        boolean vacia
) {

    public static <T> PaginaResponse<T> of(Page<T> pagina) {
        return new PaginaResponse<>(
                pagina.getContent(),
                pagina.getNumber(),
                pagina.getSize(),
                pagina.getTotalElements(),
                pagina.getTotalPages(),
                pagina.isFirst(),
                pagina.isLast(),
                pagina.isEmpty()
        );
    }

    public static <T> PaginaResponse<T> of(List<T> elementos, Pageable pageable) {
        int inicio = Math.toIntExact(pageable.getOffset());
        int fin = Math.min(inicio + pageable.getPageSize(), elementos.size());
        List<T> contenido = inicio >= elementos.size() ? List.of() : elementos.subList(inicio, fin);
        return of(new PageImpl<>(contenido, pageable, elementos.size()));
    }
}
