package com.example.torneos.mapper;
import com.example.torneos.dto.response.ResultadoResponse;
import com.example.torneos.entity.Resultado;

public class ResultadoMapper {
    public static ResultadoResponse toResponse(Resultado resultado) {
        if (resultado == null) {
            return null;
        }
        return ResultadoResponse.builder()
                .id(resultado.getId())
                .enfrentamientoId(resultado.getEnfrentamiento() != null ? resultado.getEnfrentamiento().getId() : null)
                .ganadorId(resultado.getGanador() != null ? resultado.getGanador().getId() : null)
                .ganadorNombre(resultado.getGanador() != null ? resultado.getGanador().getNombre() : null)
                .scoreParticipante1(resultado.getScoreParticipante1())
                .scoreParticipante2(resultado.getScoreParticipante2())
                .fechaRegistro(resultado.getFechaRegistro())
                .evidenciaUrl(resultado.getEvidenciaUrl())
                .validadoPorAdmin(resultado.getValidadoPorAdmin())
                .build();
    }
}
