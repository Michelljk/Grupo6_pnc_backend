package com.example.torneos.dto.response;
import com.example.torneos.enums.EstadoEnfrentamiento;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;
@Data
@Builder
public class EnfrentamientoDTOResponse {
    private Long id;
    private String participante1Nombre;
    private String participante2Nombre;
    private String ganadorNombre;
    private Integer scoreP1;
    private Integer scoreP2;
    private LocalDateTime fechaHora;
    private EstadoEnfrentamiento estado;
}
