package com.example.torneos.dto.torneo;

import com.example.torneos.enums.EstadoTorneo;
import com.example.torneos.enums.FormatoTorneo;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class TorneoResponse {
    private Long id;
    private String nombre;
    private String juego;
    private FormatoTorneo formato;
    private Integer maxParticipantes;
    private Integer eloMinimo;
    private Integer eloMaximo;
    private EstadoTorneo estado;
    private Boolean esPremium;
    private BigDecimal costoInscripcion;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private LocalDateTime creadoEn;
    private List<Long> participantesIds;
}
