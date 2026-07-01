package com.example.torneos.dto.request;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
@Data
@Builder

public class CrearDisputaRequest {
    @NotNull(message = "El ID del usuario que reporta no puede ser nulo")
    private Long usuarioReportaId;
    @NotBlank(message = "La descripción de la disputa no puede estar vacía")
    private String descripcion;
}
