package com.example.torneos.dto;

import com.example.torneos.enums.TipoPremio;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PremioDTO {
    private Long id;

    @NotBlank
    private String nombre;

    private String descripcion;

    @NotNull
    private TipoPremio tipo;

    private Integer valorCreditos;
    private BigDecimal valorMonetario;
}
