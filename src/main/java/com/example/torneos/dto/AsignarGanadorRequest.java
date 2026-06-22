package com.example.torneos.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AsignarGanadorRequest {

    @NotNull
    private Long torneoId;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Integer posicion;
}
