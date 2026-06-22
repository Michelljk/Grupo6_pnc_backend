package com.example.torneos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PuntoJugadorDTO {
    private Long id;

    @NotNull
    private Long usuarioId;

    @NotNull
    @Min(1)
    private Integer puntos;

    private Long torneoId;
    private String descripcion;
}
