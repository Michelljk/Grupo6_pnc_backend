package com.example.torneos.dto.request;
import com.example.torneos.enums.EstadoDisputa;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class ResolverDisputaRequest {
    @NotNull(message = "El ID del administrador no puede ser nulo")
    private Long administradorResuelveId;
    @NotNull(message = "El nuevo estado de la disputa no puede ser nulo")
    private EstadoDisputa nuevoEstado;
    @NotBlank(message = "La resolución no puede estar vacía")
    private String resolucion;
}
