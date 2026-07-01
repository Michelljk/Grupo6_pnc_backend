package com.example.torneos.dto.request;

import com.example.torneos.enums.FormatoTorneo;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
public class AsignarGanadorRequest {

    @NotNull
    private Long torneoId;

    @NotNull
    private Long usuarioId;

    @NotNull
    private Integer posicion;

    @Data
    @NoArgsConstructor
    public static class TorneoRequest {

        @NotBlank(message = "El nombre es obligatorio")
        private String nombre;

        @NotBlank(message = "El juego es obligatorio")
        private String juego;

        @NotNull(message = "El formato es obligatorio")
        private FormatoTorneo formato;

        @NotNull(message = "El límite de participantes es obligatorio")
        @Min(value = 2, message = "Debe haber al menos 2 participantes")
        private Integer maxParticipantes;

        @NotNull(message = "El ELO mínimo es obligatorio")
        private Integer eloMinimo;

        @NotNull(message = "El ELO máximo es obligatorio")
        private Integer eloMaximo;

        private Boolean esPremium;

        private BigDecimal costoInscripcion;
    }
}
