package com.example.torneos.dto;

import com.example.torneos.enums.ConceptoPago;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CrearPagoRequest {

    @NotNull
    private Long usuarioId;

    @NotNull
    @DecimalMin("0.50")
    private BigDecimal monto;

    @NotBlank
    private String moneda;

    @NotNull
    private ConceptoPago concepto;

    private Long torneoId;
    private Integer creditosComprados;
}
