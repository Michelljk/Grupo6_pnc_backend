package com.example.torneos.dto.response;
import com.example.torneos.enums.EstadoDisputa;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class DisputaResponse {
    private Long id;
    private Long resultadoId;
    private Long usuarioReportaId;
    private String usuarioReportaNombre;
    private String descripcion;
    private LocalDateTime fechaReporte;
    private EstadoDisputa estado;
    private String resolucion;
    private Long administradorResuelveId;
    private String administradorResuelveNombre;
}
