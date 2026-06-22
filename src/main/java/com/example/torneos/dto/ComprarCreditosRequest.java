package com.example.torneos.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComprarCreditosRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    @Min(1)
    private Integer cantidadCreditos;
}
