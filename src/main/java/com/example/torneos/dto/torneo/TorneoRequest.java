package com.example.torneos.dto.torneo;

import com.example.torneos.enums.FormatoTorneo;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class TorneoRequest {
    @NotBlank(message = "El nombre del torneo no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El juego no puede estar vacío")
    private String juego;

    @NotNull(message = "El formato del torneo no puede ser nulo")
    private FormatoTorneo formato;

    @NotNull(message = "El número máximo de participantes no puede ser nulo")
    @Positive(message = "El número máximo de participantes debe ser positivo")
    private Integer maxParticipantes;

    @PositiveOrZero(message = "El ELO mínimo debe ser cero o positivo")
    private Integer eloMinimo;

    @PositiveOrZero(message = "El ELO máximo debe ser cero o positivo")
    private Integer eloMaximo;

    private Boolean esPremium;

    private BigDecimal costoInscripcion;

    @NotNull(message = "La fecha de inicio no puede ser nula")
    private LocalDateTime fechaInicio;

    private LocalDateTime fechaFin;
}
