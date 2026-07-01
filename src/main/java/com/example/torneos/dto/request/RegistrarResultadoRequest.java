package com.example.torneos.dto.request;
import jakarta.validation.constraints. NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder

public class RegistrarResultadoRequest {
    @NotNull(message = "El ID del ganador no puede ser null")
    private Long ganadorId;
    private Integer scoreP1;
    private Integer scoreP2;
    private String evidenciaUrl;
}
