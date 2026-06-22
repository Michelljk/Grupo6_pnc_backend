package com.example.torneos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PremioTorneoDTO {
    private Long id;

    @NotNull
    private Long torneoId;

    @NotNull
    private Long premioId;

    @NotNull
    private Integer posicion;

    private Boolean asignado;
    private Long usuarioGanadorId;
    private String nombrePremio;
    private String nombreTorneo;
}
