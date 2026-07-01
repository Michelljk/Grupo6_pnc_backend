package com.example.torneos.mapper;
import com.example.torneos.dto.response.DisputaResponse;
import com.example.torneos.entity.Disputa;

public class DisputaMapper {
    public static DisputaResponse toResponse(Disputa disputa) {
        if (disputa == null) {
            return null;
        }
        return DisputaResponse.builder()
                .id(disputa.getId())
                .resultadoId(disputa.getResultado() != null ? disputa.getResultado().getId() : null)
                .usuarioReportaId(disputa.getUsuarioReporta() != null ? disputa.getUsuarioReporta().getId() : null)
                .usuarioReportaNombre(disputa.getUsuarioReporta() != null ? disputa.getUsuarioReporta().getNombre() : null)
                .descripcion(disputa.getDescripcion())
                .fechaReporte(disputa.getFechaReporte())
                .estado(disputa.getEstado())
                .resolucion(disputa.getResolucion())
                .administradorResuelveId(disputa.getAdministradorResuelve() != null ? disputa.getAdministradorResuelve().getId() : null)
                .administradorResuelveNombre(disputa.getAdministradorResuelve() != null ? disputa.getAdministradorResuelve().getNombre() : null)
                .build();
    }
}
