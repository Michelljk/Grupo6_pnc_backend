package com.example.torneos.dto.response;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class ResultadoResponse {
    private Long id;
    private Long enfrentamientoId;
    private Long ganadorId;
    private String ganadorNombre;
    private Integer scoreParticipante1;
    private Integer scoreParticipante2;
    private LocalDateTime fechaRegistro;
    private String evidenciaUrl;
    private Boolean validadoPorAdmin;
}
