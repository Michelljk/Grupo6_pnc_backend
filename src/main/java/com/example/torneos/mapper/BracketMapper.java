package com.example.torneos.mapper;
import com.example.torneos.dto.response.*;
import com.example.torneos.entity.*;
import java.util.stream.Collectors;

public class BracketMapper {
    public static BracketDTOResponse toResponse(Bracket bracket) {
        return BracketDTOResponse.builder()
                .id(bracket.getId())
                .torneoId(bracket.getTorneo().getId())
                .estado(bracket.getEstado())
                .fechaCreacion(bracket.getFechaCreacion())
                .rondas(bracket.getRondas() != null ? bracket.getRondas().stream().map(BracketMapper::toRondaResponse).collect(Collectors.toList()) : java.util.Collections.emptyList())
                .build();
    }
    public static RondaDTOResponse toRondaResponse(Ronda ronda) {
        return RondaDTOResponse.builder()
                .id(ronda.getId())
                .numeroRonda(ronda.getNumeroRonda())
                .nombre(ronda.getNombre())
                .estado(ronda.getEstado())
                .enfrentamientos(ronda.getEnfrentamientos() != null ? ronda.getEnfrentamientos().stream().map(BracketMapper::toEnfrentamientoResponse).collect(Collectors.toList()) : java.util.Collections.emptyList())
                .build();
    }
    public static EnfrentamientoDTOResponse toEnfrentamientoResponse(Enfrentamiento e) {
        return EnfrentamientoDTOResponse.builder()
                .id(e.getId())
                .participante1Nombre(e.getParticipante1() != null ? e.getParticipante1().getNombre() : "TBD")
                .participante2Nombre(e.getParticipante2() != null ? e.getParticipante2().getNombre() : "TBD")
                .ganadorNombre(e.getGanador() != null ? e.getGanador().getNombre() : null)
                .scoreP1(e.getScoreParticipante1())
                .scoreP2(e.getScoreParticipante2())
                .fechaHora(e.getFechaHora())
                .estado(e.getEstado())
                .build();
    }
}
